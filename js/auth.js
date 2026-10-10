// Authentication & Session Manager

const Auth = {
    currentUser: null,

    async checkSession() {
        try {
            const res = await API.get('/api/auth/check');
            if (res.success && res.data) {
                this.currentUser = res.data;
                this.updateNavbar(res.data);
                return res.data;
            } else {
                this.currentUser = null;
                this.updateNavbar(null);
                return null;
            }
        } catch (err) {
            this.currentUser = null;
            this.updateNavbar(null);
            return null;
        }
    },

    async requireAuth() {
        const user = await this.checkSession();
        if (!user) {
            const currentPath = window.location.pathname.split('/').pop() || 'index.html';
            window.location.href = `login.html?redirect=${encodeURIComponent(currentPath)}`;
            return null;
        }
        return user;
    },

    updateNavbar(user) {
        const authNav = document.getElementById('navbar-auth-actions');
        const protectedLinks = document.querySelectorAll('.protected-nav-link');

        if (!authNav) return;

        if (user) {
            // Show protected nav links (Dashboard, My Appointments, Profile)
            protectedLinks.forEach(el => el.style.display = 'block');

            const initials = user.fullName
                ? user.fullName.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase()
                : 'PT';

            authNav.innerHTML = `
                <div class="user-badge">
                    <div class="user-avatar">${escapeHtml(initials)}</div>
                    <span class="user-name">${escapeHtml(user.fullName)}</span>
                </div>
                <button class="btn btn-secondary btn-sm" id="logout-nav-btn">Log Out</button>
            `;

            document.getElementById('logout-nav-btn')?.addEventListener('click', () => this.logout());
        } else {
            // Hide protected nav links
            protectedLinks.forEach(el => el.style.display = 'none');

            authNav.innerHTML = `
                <a href="login.html" class="btn btn-secondary btn-sm">Log In</a>
                <a href="register.html" class="btn btn-primary btn-sm">Register</a>
            `;
        }
    },

    async logout() {
        try {
            await API.post('/api/auth/logout', {});
            showToast('You have been logged out successfully.', 'info');
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 600);
        } catch (err) {
            window.location.href = 'index.html';
        }
    }
};

// Global Init for all pages
document.addEventListener('DOMContentLoaded', () => {
    // If not on an explicit auth-checking page that calls requireAuth, check session anyway to render navbar
    if (!window.skipAutoAuthCheck) {
        Auth.checkSession();
    }
});
