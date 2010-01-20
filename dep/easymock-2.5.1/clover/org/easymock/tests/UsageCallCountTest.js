var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":144,"id":4743,"methods":[{"el":39,"sc":5,"sl":35},{"el":45,"sc":5,"sl":41},{"el":51,"sc":5,"sl":47},{"el":58,"sc":5,"sl":53},{"el":66,"sc":5,"sl":60},{"el":74,"sc":5,"sl":68},{"el":82,"sc":5,"sl":76},{"el":90,"sc":5,"sl":84},{"el":98,"sc":5,"sl":92},{"el":102,"sc":5,"sl":100},{"el":107,"sc":5,"sl":104},{"el":113,"sc":5,"sl":109},{"el":121,"sc":5,"sl":115},{"el":129,"sc":5,"sl":123},{"el":143,"sc":5,"sl":131}],"name":"UsageCallCountTest","sl":25},{"el":33,"id":4743,"methods":[],"name":"UsageCallCountTest.VoidMethodInterface","sl":31}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_161":{"methods":[{"sl":53},{"sl":100},{"sl":115}],"name":"mockWithOneExpectedCallFailsAtVerify","pass":true,"statements":[{"sl":55},{"sl":56},{"sl":57},{"sl":101},{"sl":116},{"sl":117}]},"test_259":{"methods":[{"sl":41}],"name":"mockWithNoExpectedCallsPassesWithNoCalls","pass":true,"statements":[{"sl":43},{"sl":44}]},"test_301":{"methods":[{"sl":92},{"sl":109},{"sl":123}],"name":"tooManyCalls","pass":true,"statements":[{"sl":94},{"sl":95},{"sl":96},{"sl":97},{"sl":110},{"sl":111},{"sl":112},{"sl":124},{"sl":125}]},"test_340":{"methods":[{"sl":47},{"sl":123}],"name":"mockWithNoExpectedCallsFailsAtFirstCall","pass":true,"statements":[{"sl":49},{"sl":50},{"sl":124},{"sl":125}]},"test_448":{"methods":[{"sl":115},{"sl":131}],"name":"noUpperLimitWithoutCallCountSet","pass":true,"statements":[{"sl":116},{"sl":117},{"sl":133},{"sl":134},{"sl":135},{"sl":136},{"sl":137},{"sl":138},{"sl":139},{"sl":140},{"sl":141},{"sl":142}]},"test_491":{"methods":[{"sl":76},{"sl":104},{"sl":109},{"sl":115}],"name":"tooFewCalls","pass":true,"statements":[{"sl":78},{"sl":79},{"sl":80},{"sl":81},{"sl":105},{"sl":106},{"sl":110},{"sl":111},{"sl":112},{"sl":116},{"sl":117}]},"test_512":{"methods":[{"sl":84},{"sl":109}],"name":"correctNumberOfCalls","pass":true,"statements":[{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":110},{"sl":111},{"sl":112}]},"test_56":{"methods":[{"sl":76},{"sl":104},{"sl":109},{"sl":115}],"name":"tooFewCalls","pass":true,"statements":[{"sl":78},{"sl":79},{"sl":80},{"sl":81},{"sl":105},{"sl":106},{"sl":110},{"sl":111},{"sl":112},{"sl":116},{"sl":117}]},"test_68":{"methods":[{"sl":115},{"sl":131}],"name":"noUpperLimitWithoutCallCountSet","pass":true,"statements":[{"sl":116},{"sl":117},{"sl":133},{"sl":134},{"sl":135},{"sl":136},{"sl":137},{"sl":138},{"sl":139},{"sl":140},{"sl":141},{"sl":142}]},"test_708":{"methods":[{"sl":84},{"sl":109}],"name":"correctNumberOfCalls","pass":true,"statements":[{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":110},{"sl":111},{"sl":112}]},"test_716":{"methods":[{"sl":60},{"sl":100}],"name":"mockWithOneExpectedCallPassesWithOneCall","pass":true,"statements":[{"sl":62},{"sl":63},{"sl":64},{"sl":65},{"sl":101}]},"test_725":{"methods":[{"sl":47},{"sl":123}],"name":"mockWithNoExpectedCallsFailsAtFirstCall","pass":true,"statements":[{"sl":49},{"sl":50},{"sl":124},{"sl":125}]},"test_74":{"methods":[{"sl":68},{"sl":100},{"sl":123}],"name":"mockWithOneExpectedCallFailsAtSecondCall","pass":true,"statements":[{"sl":70},{"sl":71},{"sl":72},{"sl":73},{"sl":101},{"sl":124},{"sl":125}]},"test_791":{"methods":[{"sl":53},{"sl":100},{"sl":115}],"name":"mockWithOneExpectedCallFailsAtVerify","pass":true,"statements":[{"sl":55},{"sl":56},{"sl":57},{"sl":101},{"sl":116},{"sl":117}]},"test_849":{"methods":[{"sl":41}],"name":"mockWithNoExpectedCallsPassesWithNoCalls","pass":true,"statements":[{"sl":43},{"sl":44}]},"test_89":{"methods":[{"sl":68},{"sl":100},{"sl":123}],"name":"mockWithOneExpectedCallFailsAtSecondCall","pass":true,"statements":[{"sl":70},{"sl":71},{"sl":72},{"sl":73},{"sl":101},{"sl":124},{"sl":125}]},"test_915":{"methods":[{"sl":92},{"sl":109},{"sl":123}],"name":"tooManyCalls","pass":true,"statements":[{"sl":94},{"sl":95},{"sl":96},{"sl":97},{"sl":110},{"sl":111},{"sl":112},{"sl":124},{"sl":125}]},"test_936":{"methods":[{"sl":60},{"sl":100}],"name":"mockWithOneExpectedCallPassesWithOneCall","pass":true,"statements":[{"sl":62},{"sl":63},{"sl":64},{"sl":65},{"sl":101}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [259, 849], [], [259, 849], [259, 849], [], [], [725, 340], [], [725, 340], [725, 340], [], [], [791, 161], [], [791, 161], [791, 161], [791, 161], [], [], [716, 936], [], [716, 936], [716, 936], [716, 936], [716, 936], [], [], [74, 89], [], [74, 89], [74, 89], [74, 89], [74, 89], [], [], [491, 56], [], [491, 56], [491, 56], [491, 56], [491, 56], [], [], [512, 708], [], [512, 708], [512, 708], [512, 708], [512, 708], [], [], [915, 301], [], [915, 301], [915, 301], [915, 301], [915, 301], [], [], [791, 716, 74, 161, 89, 936], [791, 716, 74, 161, 89, 936], [], [], [491, 56], [491, 56], [491, 56], [], [], [491, 512, 915, 708, 301, 56], [491, 512, 915, 708, 301, 56], [491, 512, 915, 708, 301, 56], [491, 512, 915, 708, 301, 56], [], [], [791, 448, 491, 161, 68, 56], [791, 448, 491, 161, 68, 56], [791, 448, 491, 161, 68, 56], [], [], [], [], [], [74, 725, 340, 915, 89, 301], [74, 725, 340, 915, 89, 301], [74, 725, 340, 915, 89, 301], [], [], [], [], [], [448, 68], [], [448, 68], [448, 68], [448, 68], [448, 68], [448, 68], [448, 68], [448, 68], [448, 68], [448, 68], [448, 68], [], []]
