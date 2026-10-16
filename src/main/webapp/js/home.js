(function () {
    const editor = document.getElementById('homeCode');
    const preview = document.getElementById('homePreview');
    const useButton = document.getElementById('useThisCode');
    if (!editor || !preview) return;

    let timer;
    function render() { preview.srcdoc = editor.value; }
    editor.addEventListener('input', function () {
        window.clearTimeout(timer);
        timer = window.setTimeout(render, 180);
    });
    if (useButton) {
        useButton.addEventListener('click', function () {
            try { sessionStorage.setItem('codekongDraft', editor.value); } catch (e) { /* storage unavailable */ }
        });
    }
    render();
}());
