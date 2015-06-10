function checkExtension(filename, type, idwarning) {

    // Use a regular expression to trim everything before final dot
    var extension = filename.replace(/^.*\./, '');

    // Iff there is no dot anywhere in filename, we would have extension == filename,
    // so we account for this possibility now
    if (extension == filename) {
        extension = '';
    } else {
        // if there is an extension, we convert to lower case
        // (N.B. this conversion will not effect the value of the extension
        // on the file upload.)
        extension = extension.toLowerCase();
    }

    switch (extension) {
        case type:
        	$(idwarning).hide();
        	return true;
        	break;
        default:
        	$(idwarning).show();
        	return false;
        	break;
    }
}