document.addEventListener('DOMContentLoaded', () => {

    // ============================================================
    // Alert Auto-Dismiss
    // ============================================================
    const alerts = document.querySelectorAll('[class*="bg-green-50"], [class*="bg-red-50"]');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });

    // ============================================================
    // Sidebar Active Link Highlighting
    // ============================================================
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar-link').forEach(link => {
        const href = link.getAttribute('href');
        // Match exact path or parent path
        if (href === currentPath || currentPath.startsWith(href + '/')) {
            link.classList.add('active');
        }
    });

    // ============================================================
    // Amount Input Formatter
    // ============================================================
    document.querySelectorAll('input[type="number"]').forEach(input => {
        input.addEventListener('input', () => {
            if (parseFloat(input.value) < 0) {
                input.value = '';
            }
        });
    });

    // ============================================================
    // Form Validation Helper
    // ============================================================
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            const inputs = this.querySelectorAll('input[required]');
            let isValid = true;
            inputs.forEach(input => {
                if (!input.value.trim()) {
                    input.classList.add('ring-2', 'ring-red-500');
                    isValid = false;
                } else {
                    input.classList.remove('ring-2', 'ring-red-500');
                }
            });
            return isValid;
        });
    });

    // ============================================================
    // Dark Mode Aware Content
    // ============================================================
    const updateDarkModeContent = () => {
        const isDark = document.documentElement.classList.contains('dark');
        // Add any dark-mode-specific logic here
        if (isDark) {
            document.body.style.colorScheme = 'dark';
        } else {
            document.body.style.colorScheme = 'light';
        }
    };

    // Initial call
    updateDarkModeContent();

    // Watch for dark mode changes
    const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
            if (mutation.attributeName === 'class') {
                updateDarkModeContent();
            }
        });
    });

    observer.observe(document.documentElement, { attributes: true });

});