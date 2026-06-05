// ── Student Portal · main.js ──

// Read a query parameter from the current URL
function getParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

// Render an alert banner inside #alert-box (must exist in the page)
function showAlert(message, type = 'info') {
  const box = document.getElementById('alert-box');
  if (!box) return;
  const icons = { danger: '✖', success: '✔', info: 'ℹ' };
  box.innerHTML = `
    <div class="alert alert-${type}">
      <span>${icons[type] || 'ℹ'}</span>
      <span>${message}</span>
    </div>`;
}

// Handle error / success query params on login & register pages
function handleAuthMessages() {
  const errors = {
    missing:   'Please fill in all required fields.',
    invalid:   'Incorrect username or password.',
    session:   'Your session has expired. Please log in again.',
    shortpass: 'Password must be at least 6 characters.',
    exists:    'That username is already taken — please choose another.',
  };
  const msgs = {
    registered: 'Account created! You can now log in.',
    loggedout:  'You have been logged out.',
  };

  const err = getParam('error');
  const msg = getParam('msg');
  if (err && errors[err]) showAlert(errors[err], 'danger');
  if (msg && msgs[msg])   showAlert(msgs[msg],   'success');
}

// ── Dashboard session guard ──
// Call on main.html load; redirects to login if no session cookie found
function guardDashboard() {
  // Because we rely on server-side sessions (HttpSession), the servlet already
  // redirects unauthenticated requests.  This JS guard simply wires up the UI:
  const username = getParam('user');
  if (!username) {
    window.location.href = 'login.html?error=session';
    return null;
  }
  return username;
}

// ── Tab switching for dashboard ──
function initTabs() {
  const navItems = document.querySelectorAll('.nav-item[data-tab]');
  const panels   = document.querySelectorAll('.tab-panel');

  function activate(tabId) {
    navItems.forEach(n => n.classList.toggle('active', n.dataset.tab === tabId));
    panels.forEach(p => {
      p.style.display = p.id === tabId ? 'block' : 'none';
    });
    localStorage.setItem('sp_tab', tabId);
  }

  navItems.forEach(n => n.addEventListener('click', () => activate(n.dataset.tab)));

  // Restore last tab or default to 'overview'
  const saved = localStorage.getItem('sp_tab') || 'overview';
  activate(saved);
}

// ── Animate progress bars ──
function animateProgress() {
  document.querySelectorAll('.progress-fill[data-pct]').forEach(el => {
    const pct = el.dataset.pct;
    setTimeout(() => { el.style.width = pct + '%'; }, 200);
  });
}

// ── Populate user info in sidebar/topbar ──
function populateUser(username) {
  document.querySelectorAll('.js-username').forEach(el => el.textContent = username);
  document.querySelectorAll('.js-avatar').forEach(el => {
    el.textContent = username.slice(0, 2).toUpperCase();
  });
}

// Auto-run on DOMContentLoaded
document.addEventListener('DOMContentLoaded', () => {
  handleAuthMessages();

  if (document.body.dataset.page === 'dashboard') {
    const username = guardDashboard();
    if (username) {
      populateUser(username);
      initTabs();
      animateProgress();
    }
  }
});