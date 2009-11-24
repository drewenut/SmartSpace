var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":40,"id":2122,"methods":[{"el":30,"sc":5,"sl":28},{"el":34,"sc":5,"sl":32},{"el":39,"sc":5,"sl":36}],"name":"Matches","sl":22}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_261":{"methods":[{"sl":28},{"sl":32}],"name":"testMatches","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_32":{"methods":[{"sl":28},{"sl":36}],"name":"matchesToString","pass":true,"statements":[{"sl":29},{"sl":37}]},"test_747":{"methods":[{"sl":28},{"sl":36}],"name":"matchesToString","pass":true,"statements":[{"sl":29},{"sl":37}]},"test_752":{"methods":[{"sl":28},{"sl":32}],"name":"testMatches","pass":true,"statements":[{"sl":29},{"sl":33}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [752, 261, 747, 32], [752, 261, 747, 32], [], [], [752, 261], [752, 261], [], [], [747, 32], [747, 32], [], [], []]
