function createCopyToClipboardElement() {
    var copyToClipboardDiv = document.createElement('div');
    var copyToClipboardSpan = document.createElement('span');
    copyToClipboardSpan.setAttribute('class', 'copytoclipboard');
    copyToClipboardSpan.setAttribute('onclick', 'copyToClipboard(this);');
    copyToClipboardSpan.innerText = 'Copy to Clipboard';
    copyToClipboardDiv.appendChild(copyToClipboardSpan);
    return copyToClipboardDiv;
}

function copyText(element) {
    var range, selection;

    if (document.body.createTextRange) {
        range = document.body.createTextRange();
        range.moveToElementText(element);
        range.select();
    } else if (window.getSelection) {
        selection = window.getSelection();
        range = document.createRange();
        range.selectNodeContents(element);
        selection.removeAllRanges();
        selection.addRange(range);
    }

    try {
        document.execCommand('copy');
    }
    catch (e) {
        console.error('unable to copy text');
    }
}

function copyToClipboard(el) {
    copyText(el.parentNode.previousElementSibling);
}

document.addEventListener('DOMContentLoaded', function(event) {
    var elements = document.getElementsByClassName('listingblock');
    for (var i = 0; i < elements.length; i++) {
        elements[i].appendChild(createCopyToClipboardElement());
    }
});
