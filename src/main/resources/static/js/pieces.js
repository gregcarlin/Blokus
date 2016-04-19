
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
	if (_.isEqual(rot, flipVert([1,0,0,1,1]))) return 4;
	if (_.isEqual(rot, rotLeft(flipVert([1,0,0,1,1])))) return 5;
	if (_.isEqual(rot, rotLeft(rotLeft(flipVert([1,0,0,1,1]))))) return 6;
	if (_.isEqual(rot, rotLeft(rotLeft(rotLeft(flipVert([1,0,0,1,1])))))) return 7;
}
