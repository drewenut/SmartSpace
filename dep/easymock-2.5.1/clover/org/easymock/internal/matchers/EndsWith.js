var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":39,"id":2051,"methods":[{"el":30,"sc":5,"sl":28},{"el":34,"sc":5,"sl":32},{"el":38,"sc":5,"sl":36}],"name":"EndsWith","sl":22}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_20":{"methods":[{"sl":28},{"sl":32}],"name":"testEndsWith","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_504":{"methods":[{"sl":28},{"sl":36}],"name":"endsWithToString","pass":true,"statements":[{"sl":29},{"sl":37}]},"test_670":{"methods":[{"sl":28},{"sl":36}],"name":"endsWithToString","pass":true,"statements":[{"sl":29},{"sl":37}]},"test_700":{"methods":[{"sl":28},{"sl":32}],"name":"testEndsWith","pass":true,"statements":[{"sl":29},{"sl":33}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [20, 504, 700, 670], [20, 504, 700, 670], [], [], [20, 700], [20, 700], [], [], [504, 670], [504, 670], [], []]
