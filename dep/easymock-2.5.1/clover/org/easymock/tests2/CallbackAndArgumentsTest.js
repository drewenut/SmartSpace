var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":97,"id":2253,"methods":[{"el":33,"sc":5,"sl":30},{"el":56,"sc":5,"sl":35},{"el":45,"sc":13,"sl":42},{"el":61,"sc":5,"sl":58},{"el":96,"sc":5,"sl":63},{"el":74,"sc":13,"sl":71},{"el":83,"sc":13,"sl":79}],"name":"CallbackAndArgumentsTest","sl":26}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_155":{"methods":[{"sl":35},{"sl":42}],"name":"callbackGetsArguments","pass":true,"statements":[{"sl":38},{"sl":40},{"sl":41},{"sl":43},{"sl":44},{"sl":48},{"sl":50},{"sl":51},{"sl":53},{"sl":55}]},"test_177":{"methods":[{"sl":58}],"name":"currentArgumentsFailsOutsideCallbacks","pass":true,"statements":[{"sl":60}]},"test_256":{"methods":[{"sl":63},{"sl":71},{"sl":79}],"name":"callbackGetsArgumentsEvenIfAMockCallsAnother","pass":true,"statements":[{"sl":66},{"sl":68},{"sl":69},{"sl":70},{"sl":73},{"sl":77},{"sl":78},{"sl":80},{"sl":81},{"sl":82},{"sl":86},{"sl":87},{"sl":89},{"sl":90},{"sl":92},{"sl":93},{"sl":95}]},"test_537":{"methods":[{"sl":63},{"sl":71},{"sl":79}],"name":"callbackGetsArgumentsEvenIfAMockCallsAnother","pass":true,"statements":[{"sl":66},{"sl":68},{"sl":69},{"sl":70},{"sl":73},{"sl":77},{"sl":78},{"sl":80},{"sl":81},{"sl":82},{"sl":86},{"sl":87},{"sl":89},{"sl":90},{"sl":92},{"sl":93},{"sl":95}]},"test_719":{"methods":[{"sl":35},{"sl":42}],"name":"callbackGetsArguments","pass":true,"statements":[{"sl":38},{"sl":40},{"sl":41},{"sl":43},{"sl":44},{"sl":48},{"sl":50},{"sl":51},{"sl":53},{"sl":55}]},"test_875":{"methods":[{"sl":58}],"name":"currentArgumentsFailsOutsideCallbacks","pass":true,"statements":[{"sl":60}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [719, 155], [], [], [719, 155], [], [719, 155], [719, 155], [719, 155], [719, 155], [719, 155], [], [], [], [719, 155], [], [719, 155], [719, 155], [], [719, 155], [], [719, 155], [], [], [177, 875], [], [177, 875], [], [], [537, 256], [], [], [537, 256], [], [537, 256], [537, 256], [537, 256], [537, 256], [], [537, 256], [], [], [], [537, 256], [537, 256], [537, 256], [537, 256], [537, 256], [537, 256], [], [], [], [537, 256], [537, 256], [], [537, 256], [537, 256], [], [537, 256], [537, 256], [], [537, 256], [], []]
