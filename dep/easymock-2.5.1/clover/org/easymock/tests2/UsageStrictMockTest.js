var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":211,"id":3373,"methods":[{"el":36,"sc":5,"sl":30},{"el":43,"sc":5,"sl":38},{"el":56,"sc":5,"sl":45},{"el":72,"sc":5,"sl":58},{"el":88,"sc":5,"sl":74},{"el":138,"sc":5,"sl":90},{"el":178,"sc":5,"sl":140},{"el":210,"sc":5,"sl":180}],"name":"UsageStrictMockTest","sl":27}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_176":{"methods":[{"sl":74}],"name":"tooFewCallsFailure","pass":true,"statements":[{"sl":76},{"sl":77},{"sl":78},{"sl":79},{"sl":81},{"sl":82},{"sl":85}]},"test_200":{"methods":[{"sl":140}],"name":"range","pass":true,"statements":[{"sl":143},{"sl":145},{"sl":146},{"sl":147},{"sl":148},{"sl":149},{"sl":150},{"sl":151},{"sl":152},{"sl":154},{"sl":156},{"sl":157},{"sl":159},{"sl":160},{"sl":161},{"sl":163},{"sl":165},{"sl":166},{"sl":168},{"sl":169},{"sl":175}]},"test_272":{"methods":[{"sl":38}],"name":"orderedCallsSucces","pass":true,"statements":[{"sl":40},{"sl":41},{"sl":42}]},"test_544":{"methods":[{"sl":180}],"name":"stubBehavior","pass":true,"statements":[{"sl":182},{"sl":184},{"sl":185},{"sl":186},{"sl":187},{"sl":189},{"sl":191},{"sl":192},{"sl":193},{"sl":194},{"sl":195},{"sl":197},{"sl":198},{"sl":199},{"sl":201},{"sl":202},{"sl":207}]},"test_545":{"methods":[{"sl":90}],"name":"differentMethods","pass":true,"statements":[{"sl":93},{"sl":95},{"sl":96},{"sl":97},{"sl":98},{"sl":99},{"sl":100},{"sl":101},{"sl":103},{"sl":104},{"sl":105},{"sl":107},{"sl":108},{"sl":109},{"sl":111},{"sl":112},{"sl":119},{"sl":123},{"sl":125},{"sl":126},{"sl":127},{"sl":129},{"sl":130},{"sl":135}]},"test_59":{"methods":[{"sl":90}],"name":"differentMethods","pass":true,"statements":[{"sl":93},{"sl":95},{"sl":96},{"sl":97},{"sl":98},{"sl":99},{"sl":100},{"sl":101},{"sl":103},{"sl":104},{"sl":105},{"sl":107},{"sl":108},{"sl":109},{"sl":111},{"sl":112},{"sl":119},{"sl":123},{"sl":125},{"sl":126},{"sl":127},{"sl":129},{"sl":130},{"sl":135}]},"test_6":{"methods":[{"sl":58}],"name":"tooManyCallsFailure","pass":true,"statements":[{"sl":60},{"sl":61},{"sl":63},{"sl":64},{"sl":65},{"sl":67},{"sl":69}]},"test_64":{"methods":[{"sl":74}],"name":"tooFewCallsFailure","pass":true,"statements":[{"sl":76},{"sl":77},{"sl":78},{"sl":79},{"sl":81},{"sl":82},{"sl":85}]},"test_662":{"methods":[{"sl":180}],"name":"stubBehavior","pass":true,"statements":[{"sl":182},{"sl":184},{"sl":185},{"sl":186},{"sl":187},{"sl":189},{"sl":191},{"sl":192},{"sl":193},{"sl":194},{"sl":195},{"sl":197},{"sl":198},{"sl":199},{"sl":201},{"sl":202},{"sl":207}]},"test_727":{"methods":[{"sl":45}],"name":"unorderedCallsFailure","pass":true,"statements":[{"sl":47},{"sl":48},{"sl":49},{"sl":51},{"sl":53}]},"test_735":{"methods":[{"sl":140}],"name":"range","pass":true,"statements":[{"sl":143},{"sl":145},{"sl":146},{"sl":147},{"sl":148},{"sl":149},{"sl":150},{"sl":151},{"sl":152},{"sl":154},{"sl":156},{"sl":157},{"sl":159},{"sl":160},{"sl":161},{"sl":163},{"sl":165},{"sl":166},{"sl":168},{"sl":169},{"sl":175}]},"test_737":{"methods":[{"sl":58}],"name":"tooManyCallsFailure","pass":true,"statements":[{"sl":60},{"sl":61},{"sl":63},{"sl":64},{"sl":65},{"sl":67},{"sl":69}]},"test_832":{"methods":[{"sl":38}],"name":"orderedCallsSucces","pass":true,"statements":[{"sl":40},{"sl":41},{"sl":42}]},"test_933":{"methods":[{"sl":45}],"name":"unorderedCallsFailure","pass":true,"statements":[{"sl":47},{"sl":48},{"sl":49},{"sl":51},{"sl":53}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [832, 272], [], [832, 272], [832, 272], [832, 272], [], [], [933, 727], [], [933, 727], [933, 727], [933, 727], [], [933, 727], [], [933, 727], [], [], [], [], [737, 6], [], [737, 6], [737, 6], [], [737, 6], [737, 6], [737, 6], [], [737, 6], [], [737, 6], [], [], [], [], [64, 176], [], [64, 176], [64, 176], [64, 176], [64, 176], [], [64, 176], [64, 176], [], [], [64, 176], [], [], [], [], [59, 545], [], [], [59, 545], [], [59, 545], [59, 545], [59, 545], [59, 545], [59, 545], [59, 545], [59, 545], [], [59, 545], [59, 545], [59, 545], [], [59, 545], [59, 545], [59, 545], [], [59, 545], [59, 545], [], [], [], [], [], [], [59, 545], [], [], [], [59, 545], [], [59, 545], [59, 545], [59, 545], [], [59, 545], [59, 545], [], [], [], [], [59, 545], [], [], [], [], [735, 200], [], [], [735, 200], [], [735, 200], [735, 200], [735, 200], [735, 200], [735, 200], [735, 200], [735, 200], [735, 200], [], [735, 200], [], [735, 200], [735, 200], [], [735, 200], [735, 200], [735, 200], [], [735, 200], [], [735, 200], [735, 200], [], [735, 200], [735, 200], [], [], [], [], [], [735, 200], [], [], [], [], [544, 662], [], [544, 662], [], [544, 662], [544, 662], [544, 662], [544, 662], [], [544, 662], [], [544, 662], [544, 662], [544, 662], [544, 662], [544, 662], [], [544, 662], [544, 662], [544, 662], [], [544, 662], [544, 662], [], [], [], [], [544, 662], [], [], [], []]
