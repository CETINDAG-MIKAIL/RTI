const html = document.documentElement;
const toggleButton = document.getElementById('switch');


const savedTheme = localStorage.getItem('theme');


if (savedTheme === 'dark-mode') {
    html.classList.add('dark-mode');
    if(toggleButton) {
        toggleButton.checked = true;
    }
} else {
    html.classList.remove('dark-mode');
    if(toggleButton) {
        toggleButton.checked = false;
    }
}

if(toggleButton) {
    toggleButton.addEventListener('click', () => {
        if (html.classList.contains('dark-mode')) {
            html.classList.remove('dark-mode');
            localStorage.setItem('theme', 'light-mode');
        } else {
            html.classList.add('dark-mode');
            localStorage.setItem('theme', 'dark-mode');
        }
    });
}
