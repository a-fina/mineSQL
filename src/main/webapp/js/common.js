function chiediConferma(url,msg) {
	if (confirm(msg)) {
		window.location=url;
	}
}