
pieces = [
[0, 0],
[0, 0, 1, 0],
[0, 0, -1, 0, 1, 0],
[0, 0, 0, 1, 1, 0],
[0, 0, -1, 0, 1, 0, 2, 0],
[0, 0, -1, 1, -1, 0, 1, 0],
[0, 0, 0, 1, 1, 0, 1, 1],
[0, 0, -1, 0, 0, 1, 1, 0],
[0, 0, -1, 0, 0, 1, 1, 1],
[0, 0, -1, -1, 0, -1, 0, 1, 1, 0],
[0, 0, -2, 0, -1, 0, 1, 0, 2, 0],
[0, 0, -2, 0, -1, 0, 1, 0, 1, 1],
[0, 0, -2, 0, -1, 0, 0, 1, 1, 1],
[0, 0, 0, -1, 0, 1, 1, 0, 1, 1],
[0, 0, -1, -1, -1, 1, 1, 0, -1, 0],
[0, 0, 0, -1, 0, 1, 1, -1, 1, 1],
[0, 0, 0, 1, 0, 2, 1, 0, 2, 0],
[0, 0, -1, 1, -1, 0, 0, -1, 1, -1],
[0, 0, -1, 0, 0, -1, 0, 1, 1, 0],
[0, 0, -2, 0, -1, 0, 0, 1, 1, 0],
[0, 0, -1, -1, 0, -1, 0, 1, 1, 1]];

function getOrientation(rot) {
	if (_.isEqual(rot, [1,0,0,1,1])) return 0;
	if (_.isEqual(rot, rotLeft([1,0,0,1,1]))) return 1;
	if (_.isEqual(rot, rotLeft(rotLeft([1,0,0,1,1])))) return 2;
	if (_.isEqual(rot, rotLeft(rotLeft(rotLeft([1,0,0,1,1]))))) return 3;
	if (_.isEqual(rot, flipHoriz([1,0,0,1,1]))) return 4;
	if (_.isEqual(rot, rotLeft(flipHoriz([1,0,0,1,1])))) return 7;
	if (_.isEqual(rot, rotLeft(rotLeft(flipHoriz([1,0,0,1,1]))))) return 6;
	if (_.isEqual(rot, rotLeft(rotLeft(rotLeft(flipHoriz([1,0,0,1,1])))))) return 5;
}

function getRotate(n) {
	var r = [1,0,0,1,1];
  switch (n) {
    case 0: return r;
    case 1: return rotLeft(r);
    case 2: return rotLeft(rotLeft(r));
    case 3: return rotLeft(rotLeft(rotLeft(r)));
    case 4: return flipHoriz(r);
    case 7: return rotLeft(flipHoriz(r));
    case 6: return rotLeft(rotLeft(flipHoriz(r)));
    case 5: return rotLeft(rotLeft(rotLeft(flipHoriz(r))));
  }
}
