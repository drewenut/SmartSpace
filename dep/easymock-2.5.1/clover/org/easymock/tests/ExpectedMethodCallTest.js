var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":48,"id":3872,"methods":[{"el":37,"sc":5,"sl":31},{"el":47,"sc":5,"sl":39}],"name":"ExpectedMethodCallTest","sl":27}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_4":{"methods":[{"sl":39}],"name":"testHashCode","pass":true,"statements":[{"sl":41},{"sl":42},{"sl":45}]},"test_402":{"methods":[{"sl":39}],"name":"testHashCode","pass":true,"statements":[{"sl":41},{"sl":42},{"sl":45}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [402, 4], [], [402, 4], [402, 4], [], [], [402, 4], [], [], []]
