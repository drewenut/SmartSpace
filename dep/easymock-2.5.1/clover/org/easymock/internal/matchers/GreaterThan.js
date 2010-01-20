var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":35,"id":2098,"methods":[{"el":24,"sc":5,"sl":22},{"el":29,"sc":5,"sl":26},{"el":34,"sc":5,"sl":31}],"name":"GreaterThan","sl":18}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_142":{"methods":[{"sl":22},{"sl":31}],"name":"greaterThan","pass":true,"statements":[{"sl":23},{"sl":33}]},"test_263":{"methods":[{"sl":22},{"sl":31}],"name":"lessOrEqual","pass":true,"statements":[{"sl":23},{"sl":33}]},"test_347":{"methods":[{"sl":22},{"sl":31}],"name":"greaterThan","pass":true,"statements":[{"sl":23},{"sl":33}]},"test_567":{"methods":[{"sl":22},{"sl":26},{"sl":31}],"name":"testGreateThan","pass":true,"statements":[{"sl":23},{"sl":28},{"sl":33}]},"test_71":{"methods":[{"sl":22},{"sl":31}],"name":"greaterThanOverloaded","pass":true,"statements":[{"sl":23},{"sl":33}]},"test_831":{"methods":[{"sl":22},{"sl":31}],"name":"lessOrEqual","pass":true,"statements":[{"sl":23},{"sl":33}]},"test_868":{"methods":[{"sl":22},{"sl":31}],"name":"greaterThanOverloaded","pass":true,"statements":[{"sl":23},{"sl":33}]},"test_877":{"methods":[{"sl":22},{"sl":26},{"sl":31}],"name":"testGreateThan","pass":true,"statements":[{"sl":23},{"sl":28},{"sl":33}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [831, 877, 567, 347, 263, 71, 868, 142], [831, 877, 567, 347, 263, 71, 868, 142], [], [], [877, 567], [], [877, 567], [], [], [831, 877, 567, 347, 263, 71, 868, 142], [], [831, 877, 567, 347, 263, 71, 868, 142], [], []]
