var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":53,"id":2017,"methods":[{"el":38,"sc":5,"sl":34},{"el":43,"sc":5,"sl":40},{"el":51,"sc":5,"sl":45}],"name":"Compare","sl":24}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_456":{"methods":[{"sl":34},{"sl":40},{"sl":45}],"name":"compareWithComparator","pass":true,"statements":[{"sl":35},{"sl":36},{"sl":37},{"sl":41},{"sl":47},{"sl":48},{"sl":50}]},"test_509":{"methods":[{"sl":34},{"sl":40},{"sl":45}],"name":"compareWithComparator","pass":true,"statements":[{"sl":35},{"sl":36},{"sl":37},{"sl":41},{"sl":47},{"sl":48},{"sl":50}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [509, 456], [509, 456], [509, 456], [509, 456], [], [], [509, 456], [509, 456], [], [], [], [509, 456], [], [509, 456], [509, 456], [], [509, 456], [], [], []]
