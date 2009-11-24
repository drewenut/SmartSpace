var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":41,"id":2086,"methods":[{"el":31,"sc":5,"sl":29},{"el":36,"sc":5,"sl":33},{"el":40,"sc":5,"sl":38}],"name":"Find","sl":23}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_421":{"methods":[{"sl":29},{"sl":38}],"name":"findToString","pass":true,"statements":[{"sl":30},{"sl":39}]},"test_682":{"methods":[{"sl":29},{"sl":33}],"name":"testFind","pass":true,"statements":[{"sl":30},{"sl":34}]},"test_693":{"methods":[{"sl":29},{"sl":38}],"name":"findToString","pass":true,"statements":[{"sl":30},{"sl":39}]},"test_789":{"methods":[{"sl":29},{"sl":33}],"name":"testFind","pass":true,"statements":[{"sl":30},{"sl":34}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [789, 693, 421, 682], [789, 693, 421, 682], [], [], [789, 682], [789, 682], [], [], [], [693, 421], [693, 421], [], []]
