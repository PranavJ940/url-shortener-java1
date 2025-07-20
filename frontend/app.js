document.getElementById('shorten-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const url = document.getElementById('url-input').value;
    const response = await fetch('/shorten', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'url=' + encodeURIComponent(url)
    });
    const data = await response.json();
    document.getElementById('result').innerHTML = 'Short URL: <a href="' + data.shortUrl + '">' + data.shortUrl + '</a>';
});