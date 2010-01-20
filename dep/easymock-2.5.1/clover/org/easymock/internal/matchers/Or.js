var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":53,"id":2146,"methods":[{"el":32,"sc":5,"sl":30},{"el":41,"sc":5,"sl":34},{"el":52,"sc":5,"sl":43}],"name":"Or","sl":24}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_464":{"methods":[{"sl":30},{"sl":34}],"name":"testOr","pass":true,"statements":[{"sl":31},{"sl":35},{"sl":36},{"sl":37},{"sl":40}]},"test_620":{"methods":[{"sl":30},{"sl":34}],"name":"orOverloaded","pass":true,"statements":[{"sl":31},{"sl":35},{"sl":36},{"sl":37}]},"test_676":{"methods":[{"sl":30},{"sl":34}],"name":"orOverloaded","pass":true,"statements":[{"sl":31},{"sl":35},{"sl":36},{"sl":37}]},"test_748":{"methods":[{"sl":30},{"sl":43}],"name":"orToString","pass":true,"statements":[{"sl":31},{"sl":44},{"sl":45},{"sl":46},{"sl":47},{"sl":48},{"sl":51}]},"test_808":{"methods":[{"sl":30},{"sl":43}],"name":"orToString","pass":true,"statements":[{"sl":31},{"sl":44},{"sl":45},{"sl":46},{"sl":47},{"sl":48},{"sl":51}]},"test_879":{"methods":[{"sl":30},{"sl":34}],"name":"testOr","pass":true,"statements":[{"sl":31},{"sl":35},{"sl":36},{"sl":37},{"sl":40}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [676, 620, 464, 808, 748, 879], [676, 620, 464, 808, 748, 879], [], [], [676, 620, 464, 879], [676, 620, 464, 879], [676, 620, 464, 879], [676, 620, 464, 879], [], [], [464, 879], [], [], [808, 748], [808, 748], [808, 748], [808, 748], [808, 748], [808, 748], [], [], [808, 748], [], []]
