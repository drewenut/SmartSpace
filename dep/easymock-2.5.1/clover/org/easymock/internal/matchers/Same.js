var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":42,"id":2166,"methods":[{"el":31,"sc":5,"sl":29},{"el":35,"sc":5,"sl":33},{"el":41,"sc":5,"sl":37}],"name":"Same","sl":23}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_115":{"methods":[{"sl":29},{"sl":33}],"name":"testSame","pass":true,"statements":[{"sl":30},{"sl":34}]},"test_297":{"methods":[{"sl":29},{"sl":37}],"name":"sameToStringWithChar","pass":true,"statements":[{"sl":30},{"sl":38},{"sl":39},{"sl":40}]},"test_391":{"methods":[{"sl":29},{"sl":37}],"name":"sameToStringWithObject","pass":true,"statements":[{"sl":30},{"sl":38},{"sl":39},{"sl":40}]},"test_51":{"methods":[{"sl":29},{"sl":37}],"name":"sameToStringWithChar","pass":true,"statements":[{"sl":30},{"sl":38},{"sl":39},{"sl":40}]},"test_596":{"methods":[{"sl":29},{"sl":37}],"name":"sameToStringWithString","pass":true,"statements":[{"sl":30},{"sl":38},{"sl":39},{"sl":40}]},"test_655":{"methods":[{"sl":29},{"sl":37}],"name":"sameToStringWithString","pass":true,"statements":[{"sl":30},{"sl":38},{"sl":39},{"sl":40}]},"test_67":{"methods":[{"sl":29},{"sl":33}],"name":"testSame","pass":true,"statements":[{"sl":30},{"sl":34}]},"test_743":{"methods":[{"sl":29},{"sl":37}],"name":"sameToStringWithObject","pass":true,"statements":[{"sl":30},{"sl":38},{"sl":39},{"sl":40}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [655, 297, 67, 51, 391, 115, 596, 743], [655, 297, 67, 51, 391, 115, 596, 743], [], [], [67, 115], [67, 115], [], [], [655, 297, 51, 391, 596, 743], [655, 297, 51, 391, 596, 743], [655, 297, 51, 391, 596, 743], [655, 297, 51, 391, 596, 743], [], []]
