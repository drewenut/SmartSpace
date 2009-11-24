var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":39,"id":2136,"methods":[{"el":30,"sc":5,"sl":28},{"el":34,"sc":5,"sl":32},{"el":38,"sc":5,"sl":36}],"name":"NotNull","sl":22}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_155":{"methods":[{"sl":28},{"sl":32}],"name":"callbackGetsArguments","pass":true,"statements":[{"sl":33}]},"test_254":{"methods":[{"sl":36}],"name":"notNullToString","pass":true,"statements":[{"sl":37}]},"test_256":{"methods":[{"sl":32}],"name":"callbackGetsArgumentsEvenIfAMockCallsAnother","pass":true,"statements":[{"sl":33}]},"test_327":{"methods":[{"sl":32}],"name":"differentConstraintsOnSameCall","pass":true,"statements":[{"sl":33}]},"test_366":{"methods":[{"sl":36}],"name":"notNullToString","pass":true,"statements":[{"sl":37}]},"test_537":{"methods":[{"sl":32}],"name":"callbackGetsArgumentsEvenIfAMockCallsAnother","pass":true,"statements":[{"sl":33}]},"test_719":{"methods":[{"sl":28},{"sl":32}],"name":"callbackGetsArguments","pass":true,"statements":[{"sl":33}]},"test_724":{"methods":[{"sl":32}],"name":"testNotNull","pass":true,"statements":[{"sl":33}]},"test_80":{"methods":[{"sl":32}],"name":"testNotNull","pass":true,"statements":[{"sl":33}]},"test_993":{"methods":[{"sl":32}],"name":"differentConstraintsOnSameCall","pass":true,"statements":[{"sl":33}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [719, 155], [], [], [], [537, 993, 80, 724, 256, 327, 719, 155], [537, 993, 80, 724, 256, 327, 719, 155], [], [], [366, 254], [366, 254], [], []]
