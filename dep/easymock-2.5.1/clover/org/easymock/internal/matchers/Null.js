var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":38,"id":2141,"methods":[{"el":29,"sc":5,"sl":28},{"el":33,"sc":5,"sl":31},{"el":37,"sc":5,"sl":35}],"name":"Null","sl":22}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_270":{"methods":[{"sl":31}],"name":"testNull","pass":true,"statements":[{"sl":32}]},"test_29":{"methods":[{"sl":28},{"sl":31}],"name":"allKinds","pass":true,"statements":[{"sl":32}]},"test_327":{"methods":[{"sl":31}],"name":"differentConstraintsOnSameCall","pass":true,"statements":[{"sl":32}]},"test_344":{"methods":[{"sl":35}],"name":"nullToString","pass":true,"statements":[{"sl":36}]},"test_559":{"methods":[{"sl":35}],"name":"nullToString","pass":true,"statements":[{"sl":36}]},"test_580":{"methods":[{"sl":31}],"name":"testNull","pass":true,"statements":[{"sl":32}]},"test_780":{"methods":[{"sl":28},{"sl":31}],"name":"allKinds","pass":true,"statements":[{"sl":32}]},"test_993":{"methods":[{"sl":31}],"name":"differentConstraintsOnSameCall","pass":true,"statements":[{"sl":32}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [29, 780], [], [], [993, 29, 327, 780, 580, 270], [993, 29, 327, 780, 580, 270], [], [], [559, 344], [559, 344], [], []]
