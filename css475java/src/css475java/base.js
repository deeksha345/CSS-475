/* -----------------------------------------------------------------------------------------
 *              DO NOT CHANGE THIS BLOCK OF FUNCTIONS WITHOUT UNDERSTANDING USE
 * -----------------------------------------------------------------------------------------
 */

/* 
 * getParameterByName - retrieves a parameter passed on the URL of a call made to this page.  
 * 
 */
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

/*
 * sleep - used to wait time while main application is doing work
 *
 */
function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
	}

/*
 * waitOnJava - used to synchronize availability of the "app" extension object (JSInterface). It loops
 * until the interface should be available. It calls "onJavaLoaded" after the interface is available.
 *
 */
async function waitOnJava() {
	var loaded = false;
	await sleep(1000);
	var count = 0;
	while (!loaded) {
		try {
			count++;
			document.getElementById('result').innerHTML = "waiting... " + count.toString();
			loaded = app.loaded;
		}
		catch (err) {
			document.getElementById('result').innerHTML = err.message;
			await sleep(1000);			
		}
	}
	onJavaLoaded();
}

/*
 * onLoad - bootstrap loader on the page
 */
function onLoad() {
	waitOnJava();
}
