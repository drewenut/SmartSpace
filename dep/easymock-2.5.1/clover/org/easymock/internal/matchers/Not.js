var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":41,"id":2128,"methods":[{"el":30,"sc":5,"sl":28},{"el":34,"sc":5,"sl":32},{"el":40,"sc":5,"sl":36}],"name":"Not","sl":22}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_156":{"methods":[{"sl":28}],"name":"equalsMissing","pass":true,"statements":[{"sl":29}]},"test_221":{"methods":[{"sl":28}],"name":"equalsMissing","pass":true,"statements":[{"sl":29}]},"test_270":{"methods":[{"sl":28},{"sl":32}],"name":"testNull","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_321":{"methods":[{"sl":28},{"sl":32}],"name":"notOverloaded","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_322":{"methods":[{"sl":28},{"sl":32}],"name":"notOverloaded","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_37":{"methods":[{"sl":28},{"sl":36}],"name":"notToString","pass":true,"statements":[{"sl":29},{"sl":37},{"sl":38},{"sl":39}]},"test_487":{"methods":[{"sl":28},{"sl":36}],"name":"notToString","pass":true,"statements":[{"sl":29},{"sl":37},{"sl":38},{"sl":39}]},"test_580":{"methods":[{"sl":28},{"sl":32}],"name":"testNull","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_724":{"methods":[{"sl":28},{"sl":32}],"name":"testNotNull","pass":true,"statements":[{"sl":29},{"sl":33}]},"test_80":{"methods":[{"sl":28},{"sl":32}],"name":"testNotNull","pass":true,"statements":[{"sl":29},{"sl":33}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [487, 80, 724, 37, 156, 321, 580, 270, 221, 322], [487, 80, 724, 37, 156, 321, 580, 270, 221, 322], [], [], [80, 724, 321, 580, 270, 322], [80, 724, 321, 580, 270, 322], [], [], [487, 37], [487, 37], [487, 37], [487, 37], [], []]
