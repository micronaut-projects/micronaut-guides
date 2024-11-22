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

var items;
var idx;
function createIndex() {
    fetch('./feed.json')
        .then((response) => response.json())
        .then((json) => {
            items = json.items;
            idx = lunr(function () {
                this.ref('id')
                this.field('title')
                this.field('content_text')
                this.field('tags')
                items.forEach(function (doc) {
                    this.add(doc)
                }, this)
            })
            var searchFormQueryElement = document.getElementById('search-form-query')
            searchFormQueryElement.addEventListener('keydown', function(event) {
                if (event.key === 'Enter') {
                    event.preventDefault(); // Prevent the default form submission
                    search();
                }
            });
            searchFormQueryElement.addEventListener('input', function(event) {
                if (event.target.value === '') {
                    resetSearch();
                }
            });
        }).catch(error => console.error('Error loading JSON file:', error));
}

function search() {
    var searchQuery = document.getElementById("search-form-query").value;
    var results = idx.search(searchQuery);
    displayResults(searchQuery, results);
}
function resetSearch() {
    searchQuery = "";
    document.getElementById("search-form-query").value = searchQuery;
    displayResults(searchQuery, []);
}
function displayResults(query, results) {
    var q = query.trim();
    var noQuery = (q === "")
    var notResultsHtml = '<div class="guide"><div class="guide-title">No Results</a></div></div>';
    var html = htmlForResults(results);
    if (!noQuery && results.length === 0) {
        html = notResultsHtml;
    }
    var display = results.length === 0 && noQuery ? 'block' : 'none';
    var mainElement = document.getElementById('main');
    if (!(mainElement == null || mainElement == undefined)) {
        mainElement.style.display = display;
    }
    var breadcrumbsElement = document.getElementById('breadcrumbs');
    if (!(breadcrumbsElement == null || breadcrumbsElement == undefined)) {
        breadcrumbsElement.style.display = display;
    }
    var searchResultsElement = document.getElementById('searchResults');
    if (!(searchResultsElement == null || searchResultsElement == undefined)) {
        searchResultsElement.innerHTML = html;
    }
}

function htmlForResults(results) {
    var html = '<div class="guide-list">';
    for (var i = 0; i < results.length; i++) {
        var result = results[i];
        var item = findById(result.ref);
        if (!(item === null || item === undefined)) {
            html += guideSearchResult(item);
        }
    }
    html += '</div>';
    return html;
}
function findById(id) {
    for (var i = 0; i < items.length; i++) {
        if (items[i].id === id) {
            return items[i];
        }
    }
    return null;
}
function guideSearchResult(item) {
    const dateStr = item.date_published;
    const date = new Date(dateStr);
    const formattedDate = date.toISOString().split('T')[0];
    var html = '<div class="guide"><div class="guide-title"><a href="' + item.id + '.html">' + item.title + '</a></div><div class="guide-date">' + formattedDate+ ' </div><div class="guide-intro">' + item.content_text + '</div><div class="guide-tag-list"><span class="guide-tag-title">Tags: </span><span class="guide-tag">';
    for (var i = 0; i < item.tags.length; i++) {
        var tag = item.tags[i];
        html += '<a href="./' + tag + '-.html">' + tag + '</a>'
    }
    html += '</span></div></div>'
    return html;
}

createIndex();
