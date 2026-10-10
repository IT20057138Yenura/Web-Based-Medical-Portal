// Doctor Directory Scripts

let debounceTimeout = null;

async function loadSpecializations() {
    try {
        const res = await API.get('/api/doctors/specializations');
        const select = document.getElementById('specialization-filter');
        if (res.success && res.data && select) {
            res.data.forEach(spec => {
                const opt = document.createElement('option');
                opt.value = spec;
                opt.textContent = spec;
                select.appendChild(opt);
            });
        }
    } catch (err) {
        console.error('Failed to load specializations:', err);
    }
}

async function fetchDoctors() {
    const container = document.getElementById('doctors-container');
    const badge = document.getElementById('doctor-count-badge');
    const searchVal = document.getElementById('search-input')?.value.trim() || '';
    const specVal = document.getElementById('specialization-filter')?.value.trim() || '';

    container.innerHTML = `
        <div style="grid-column: 1/-1; text-align: center; padding: 40px;">
            <p style="color: var(--muted);">Searching available doctors...</p>
        </div>
    `;

    try {
        let url = '/api/doctors?';
        const params = new URLSearchParams();
        if (searchVal) params.append('search', searchVal);
        if (specVal) params.append('specialization', specVal);

        const res = await API.get(url + params.toString());
        if (res.success && res.data) {
            renderDoctors(res.data);
            if (badge) {
                badge.textContent = `${res.data.length} Doctor${res.data.length === 1 ? '' : 's'} Available`;
            }
        }
    } catch (err) {
        container.innerHTML = `
            <div class="empty-state" style="grid-column: 1/-1;">
                <h3>Unable to load doctors</h3>
                <p>${escapeHtml(err.message || 'Please check your connection and try again.')}</p>
                <button class="btn btn-secondary" onclick="fetchDoctors()">Try Again</button>
            </div>
        `;
    }
}

function renderDoctors(doctors) {
    const container = document.getElementById('doctors-container');
    if (!doctors || doctors.length === 0) {
        container.innerHTML = `
            <div class="empty-state" style="grid-column: 1/-1;">
                <div class="empty-icon">
                    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                </div>
                <h3>No Doctors Found</h3>
                <p>We couldn't find any doctors matching your search criteria. Try adjusting your filters.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = doctors.map(doc => {
        const initials = doc.doctorName
            ? doc.doctorName.replace('Dr. ', '').split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase()
            : 'DR';

        const daysFormatted = doc.availableDays ? doc.availableDays.join(', ') : 'Inquire clinic';

        return `
            <div class="doctor-card">
                <div class="doctor-card-top">
                    <div class="doctor-avatar-circle">${escapeHtml(initials)}</div>
                    <div class="doctor-info-head">
                        <h3>${escapeHtml(doc.doctorName)}</h3>
                        <span class="doctor-specialty">${escapeHtml(doc.specialization)}</span>
                    </div>
                </div>

                <div class="doctor-meta">
                    <div class="doctor-meta-row">
                        <span class="meta-label">Doctor ID</span>
                        <span class="meta-value">${escapeHtml(doc.doctorId)}</span>
                    </div>
                    <div class="doctor-meta-row">
                        <span class="meta-label">Clinic Hours</span>
                        <span class="meta-value">${escapeHtml(doc.availableTime)}</span>
                    </div>
                    <div class="doctor-meta-row">
                        <span class="meta-label">Working Days</span>
                        <span class="meta-value" style="max-width: 180px; text-align: right;">${escapeHtml(daysFormatted)}</span>
                    </div>
                </div>

                <div style="display: flex; gap: 10px; margin-top: auto; padding-top: 10px;">
                    <a href="doctor-details.html?id=${encodeURIComponent(doc.doctorId)}" class="btn btn-secondary btn-sm" style="flex: 1;">
                        View Schedule
                    </a>
                    <a href="book-appointment.html?doctorId=${encodeURIComponent(doc.doctorId)}" class="btn btn-primary btn-sm" style="flex: 1;">
                        Book Visit
                    </a>
                </div>
            </div>
        `;
    }).join('');
}

document.addEventListener('DOMContentLoaded', () => {
    loadSpecializations();
    fetchDoctors();

    document.getElementById('search-input')?.addEventListener('input', () => {
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(fetchDoctors, 300);
    });

    document.getElementById('specialization-filter')?.addEventListener('change', fetchDoctors);

    document.getElementById('reset-btn')?.addEventListener('click', () => {
        document.getElementById('search-input').value = '';
        document.getElementById('specialization-filter').value = '';
        fetchDoctors();
    });
});
