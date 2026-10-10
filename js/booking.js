// Appointment Booking Wizard Script

let doctorsList = [];
let selectedDoctor = null;
let chosenSlotTime = null;

async function initBookingWizard() {
    const user = await Auth.requireAuth();
    if (!user) return;

    // Set minimum date to today
    const todayStr = new Date().toISOString().split('T')[0];
    const dateInput = document.getElementById('appointment-date');
    dateInput.min = todayStr;
    dateInput.value = todayStr;

    await loadDoctors();

    // Check if doctor was passed via URL query
    const urlParams = new URLSearchParams(window.location.search);
    const preselectedDocId = urlParams.get('doctorId');
    if (preselectedDocId) {
        const select = document.getElementById('doctor-select');
        select.value = preselectedDocId;
        handleDoctorChange(preselectedDocId);
    }

    // Attach listeners
    document.getElementById('doctor-select').addEventListener('change', (e) => {
        handleDoctorChange(e.target.value);
    });

    dateInput.addEventListener('change', () => {
        if (selectedDoctor) {
            loadSlotsForSelectedDoctor();
        }
    });

    // Character counter for optional reason
    const reasonInput = document.getElementById('appointment-reason');
    const charCounter = document.getElementById('char-counter');
    reasonInput.addEventListener('input', () => {
        charCounter.textContent = `${reasonInput.value.length} / 500`;
    });

    // Form submit
    document.getElementById('booking-form').addEventListener('submit', handleBookingSubmit);
}

async function loadDoctors() {
    try {
        const res = await API.get('/api/doctors');
        if (res.success && res.data) {
            doctorsList = res.data;
            const select = document.getElementById('doctor-select');
            doctorsList.forEach(doc => {
                const opt = document.createElement('option');
                opt.value = doc.doctorId;
                opt.textContent = `${doc.doctorName} (${doc.specialization})`;
                select.appendChild(opt);
            });
        }
    } catch (err) {
        showToast('Failed to load doctor list.', 'error');
    }
}

function handleDoctorChange(doctorId) {
    const summaryBox = document.getElementById('doctor-summary-box');
    chosenSlotTime = null;
    document.getElementById('selected-slot-input').value = '';

    if (!doctorId) {
        selectedDoctor = null;
        summaryBox.style.display = 'none';
        resetSlotsContainer('Please select a doctor to see their working schedule.');
        return;
    }

    selectedDoctor = doctorsList.find(d => d.doctorId === doctorId);
    if (selectedDoctor) {
        document.getElementById('doc-box-name').textContent = selectedDoctor.doctorName;
        document.getElementById('doc-box-spec').textContent = selectedDoctor.specialization;
        document.getElementById('doc-box-hours').textContent = selectedDoctor.availableTime;
        document.getElementById('doc-box-days').textContent = selectedDoctor.availableDays.join(', ');
        summaryBox.style.display = 'block';

        loadSlotsForSelectedDoctor();
    }
}

async function loadSlotsForSelectedDoctor() {
    const dateInput = document.getElementById('appointment-date');
    const container = document.getElementById('slots-picker-container');
    const indicator = document.getElementById('slot-status-indicator');

    if (!selectedDoctor || !dateInput.value) return;

    container.innerHTML = `<p style="color: var(--muted); font-size: 0.9rem;">Checking slot availability...</p>`;
    indicator.textContent = 'Calculating slots...';

    try {
        const res = await API.get(`/api/doctors/${encodeURIComponent(selectedDoctor.doctorId)}/slots?date=${encodeURIComponent(dateInput.value)}`);
        if (res.success && res.data) {
            const slots = res.data;
            if (slots.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; padding: 12px;">
                        <p style="color: var(--danger); font-weight: 600; font-size: 0.9rem;">
                            Dr. ${escapeHtml(selectedDoctor.doctorName)} is not available on this day.
                        </p>
                        <p style="font-size: 0.8rem; color: var(--muted);">
                            Active consultation days: ${escapeHtml(selectedDoctor.availableDays.join(', '))}
                        </p>
                    </div>
                `;
                indicator.textContent = '0 slots available';
                return;
            }

            const freeSlots = slots.filter(s => s.available);
            indicator.textContent = `${freeSlots.length} available out of ${slots.length}`;

            container.innerHTML = `
                <div class="slots-grid" style="width: 100%;">
                    ${slots.map(s => `
                        <button type="button" 
                                class="slot-btn ${s.available ? 'available' : 'disabled'}" 
                                data-time="${escapeHtml(s.time)}"
                                ${s.available ? '' : 'disabled'}>
                            ${escapeHtml(s.time)}
                        </button>
                    `).join('')}
                </div>
            `;

            // Attach click listeners to available slot buttons
            container.querySelectorAll('.slot-btn.available').forEach(btn => {
                btn.addEventListener('click', () => {
                    container.querySelectorAll('.slot-btn').forEach(b => b.classList.remove('selected'));
                    btn.classList.add('selected');
                    chosenSlotTime = btn.getAttribute('data-time');
                    document.getElementById('selected-slot-input').value = chosenSlotTime;
                });
            });
        }
    } catch (err) {
        resetSlotsContainer(err.message || 'Error checking doctor schedule.');
    }
}

function resetSlotsContainer(message) {
    document.getElementById('slots-picker-container').innerHTML = `
        <p style="color: var(--muted); font-size: 0.9rem;">${escapeHtml(message)}</p>
    `;
    document.getElementById('slot-status-indicator').textContent = '';
    chosenSlotTime = null;
    document.getElementById('selected-slot-input').value = '';
}

async function handleBookingSubmit(e) {
    e.preventDefault();

    if (!chosenSlotTime) {
        showToast('Please click on an available time slot before submitting.', 'error');
        return;
    }

    const doctorId = document.getElementById('doctor-select').value;
    const appointmentDate = document.getElementById('appointment-date').value;
    const reason = document.getElementById('appointment-reason').value.trim();

    const submitBtn = document.getElementById('book-submit-btn');
    submitBtn.disabled = true;
    submitBtn.textContent = 'Confirming appointment...';

    try {
        const payload = {
            doctorId,
            appointmentDate,
            appointmentTime: chosenSlotTime,
            reason
        };

        const res = await API.post('/api/appointments', payload);
        if (res.success && res.data) {
            showToast('Appointment scheduled successfully!', 'success');
            setTimeout(() => {
                window.location.href = `appointment-details.html?id=${encodeURIComponent(res.data.appointmentId)}&new=true`;
            }, 800);
        }
    } catch (err) {
        submitBtn.disabled = false;
        submitBtn.innerHTML = `
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
            Confirm & Schedule Appointment
        `;
        showToast(err.message || 'Could not complete booking. Please try another slot.', 'error');
    }
}

document.addEventListener('DOMContentLoaded', initBookingWizard);
