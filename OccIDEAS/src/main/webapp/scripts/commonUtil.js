function lengthGreaterThan2(variable) {
	if (variable && variable.length > 2) {
		return variable;
	} else {
		return null;
	}
}

var safeDigest = function(obj) {
    if (!obj.$$phase) {
        try {
            obj.$digest();
        } catch (e) {}
    }
}