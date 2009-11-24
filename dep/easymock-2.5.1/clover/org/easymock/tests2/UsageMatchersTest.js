var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":33,"id":3369,"methods":[{"el":31,"sc":5,"sl":24}],"name":"UsageMatchersTest","sl":23}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_55":{"methods":[{"sl":24}],"name":"additionalMatchersFailAtReplay","pass":true,"statements":[{"sl":27},{"sl":28},{"sl":30}]},"test_91":{"methods":[{"sl":24}],"name":"additionalMatchersFailAtReplay","pass":true,"statements":[{"sl":27},{"sl":28},{"sl":30}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [91, 55], [], [], [91, 55], [91, 55], [], [91, 55], [], [], []]
