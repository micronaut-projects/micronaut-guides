function createCopyToClipboardElement(listingblock) {

    const copyToClipboardSpan = document.createElement('span');
    copyToClipboardSpan.setAttribute('class', 'copytoclipboard');
    copyToClipboardSpan.addEventListener('click', () => copyToClipboard(copyToClipboardSpan));
    copyToClipboardSpan.innerText = 'Copy';

    const copyToClipboardDiv = document.createElement('div');
    copyToClipboardDiv.appendChild(copyToClipboardSpan);

    const content = listingblock.getElementsByClassName('content')[0];
    content.prepend(copyToClipboardDiv);
}

function copyToClipboard(copyToClipboardSpan) {

    let range;
    let selection;

    const element = copyToClipboardSpan.parentNode.parentNode.querySelector('pre code');

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

        copyToClipboardSpan.blur();
        copyToClipboardSpan.innerText = 'Copied';
        setTimeout(function() {
            copyToClipboardSpan.innerText = 'Copy';
        }, 2000);
    }
    catch (e) {
        console.error('unable to copy text');
    }
}

document.addEventListener('DOMContentLoaded', function(event) {
    const elements = document.getElementsByClassName('listingblock');
    for (let i = 0; i < elements.length; i++) {
        createCopyToClipboardElement(elements[i]);
    }
});
