// My Appointments Management Scripts

let activeStatusFilter = '';

async function initAppointmentsPage() {
    const user = await Auth.requireAuth();
    if (!user) return;

    loadAppointments();

    // Tab filtering
    document.querySelectorAll('.filter-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.filter-tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            activeStatusFilter = tab.getAttribute('data-status');
            loadAppointments();
        });
    });
}

async function loadAppointments() {
    const tbody = document.getElementById('appointments-tbody');
    tbody.innerHTML = `
        <tr>
            <td colspan="7" style="text-align: center; color: var(--muted); padding: 40px;">
                Loading appointments...
            </td>
        </tr>
    `;

    try {
        let url = '/api/appointments/my';
        if (activeStatusFilter) {
            url += `?status=${encodeURIComponent(activeStatusFilter)}`;
        }

        const res = await API.get(url);
        if (res.success && res.data) {
            renderAppointmentRows(res.data);
        }
    } catch (err) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; color: var(--danger); padding: 30px;">
                    ${escapeHtml(err.message || 'Failed to fetch appointments.')}
                </td>
            </tr>
        `;
    }
}

function renderAppointmentRows(appointments) {
    const tbody = document.getElementById('appointments-tbody');

    if (!appointments || appointments.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 50px 20px;">
                    <div class="empty-state" style="padding: 0;">
                        <h3>No Appointments Found</h3>
                        <p>There are no appointments matching the selected status.</p>
                        <a href="book-appointment.html" class="btn btn-primary btn-sm">Schedule Now</a>
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = appointments.map(apt => {
        const isScheduled = apt.status === 'SCHEDULED';
        const isCancelled = apt.status === 'CANCELLED';

        return `
            <tr>
                <td><strong style="color: var(--primary);">${escapeHtml(apt.appointmentId)}</strong></td>
                <td><strong>${escapeHtml(apt.doctorName)}</strong></td>
                <td><span class="doctor-specialty">${escapeHtml(apt.specialization)}</span></td>
                <td>${escapeHtml(apt.appointmentDate)}</td>
                <td><strong>${escapeHtml(apt.appointmentTime)}</strong></td>
                <td><span class="badge badge-${apt.status.toLowerCase()}">${escapeHtml(apt.status)}</span></td>
                <td style="text-align: right;">
                    <div style="display: inline-flex; gap: 8px; justify-content: flex-end;">
                        <a href="appointment-details.html?id=${encodeURIComponent(apt.appointmentId)}" class="btn btn-secondary btn-sm" title="View Details">
                            Details
                        </a>

                        ${isScheduled ? `
                            <a href="edit-appointment.html?id=${encodeURIComponent(apt.appointmentId)}" class="btn btn-secondary btn-sm" title="Reschedule Date/Time">
                                Reschedule
                            </a>
                            <button class="btn btn-secondary btn-sm cancel-btn" data-id="${escapeHtml(apt.appointmentId)}" style="color: var(--danger);" title="Cancel Appointment">
                                Cancel
                            </button>
                        ` : ''}

                        ${isCancelled ? `
                            <button class="btn btn-danger btn-sm delete-btn" data-id="${escapeHtml(apt.appointmentId)}" title="Delete Cancelled Record">
                                Delete
                            </button>
                        ` : ''}
                    </div>
                </td>
            </tr>
        `;
    }).join('');

    // Attach Action Listeners
    tbody.querySelectorAll('.cancel-btn').forEach(btn => {
        btn.addEventListener('click', () => handleCancel(btn.getAttribute('data-id')));
    });

    tbody.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => handleDelete(btn.getAttribute('data-id')));
    });
}

async function handleCancel(appointmentId) {
    const confirmed = await showConfirmModal(
        'Cancel Appointment',
        `Are you sure you want to cancel appointment ${appointmentId}? The time slot will immediately be made available for other patients.`,
        'Yes, Cancel Appointment',
        true
    );

    if (confirmed) {
        try {
            const res = await API.patch(`/api/appointments/${encodeURIComponent(appointmentId)}/cancel`);
            if (res.success) {
                showToast(`Appointment ${appointmentId} has been cancelled.`, 'info');
                loadAppointments();
            }
        } catch (err) {
            showToast(err.message || 'Failed to cancel appointment.', 'error');
        }
    }
}

async function handleDelete(appointmentId) {
    const confirmed = await showConfirmModal(
        'Delete Appointment Record',
        `Are you sure you want to permanently remove appointment ${appointmentId} from your records?`,
        'Yes, Delete Record',
        true
    );

    if (confirmed) {
        try {
            const res = await API.delete(`/api/appointments/${encodeURIComponent(appointmentId)}`);
            if (res.success) {
                showToast(`Appointment ${appointmentId} has been removed.`, 'success');
                loadAppointments();
            }
        } catch (err) {
            showToast(err.message || 'Failed to delete appointment record.', 'error');
        }
    }
}

document.addEventListener('DOMContentLoaded', initAppointmentsPage);
