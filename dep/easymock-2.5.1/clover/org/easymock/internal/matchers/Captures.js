var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":54,"id":2006,"methods":[{"el":34,"sc":5,"sl":32},{"el":38,"sc":5,"sl":36},{"el":42,"sc":5,"sl":40},{"el":49,"sc":5,"sl":44},{"el":53,"sc":5,"sl":51}],"name":"Captures","sl":24}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_141":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureStrictControl_2133741","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_190":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureFirst","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_196":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureLast","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_209":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCapture2_2446744","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_211":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testAnd","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_212":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureNone","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_222":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCapture_2617107","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_240":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testPrimitiveVsObject","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_305":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureNonStrictControl_2133741","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_430":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testPrimitive","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_436":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCapture_2617107","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_449":{"methods":[{"sl":36},{"sl":40},{"sl":44},{"sl":51}],"name":"test","pass":true,"statements":[{"sl":37},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_453":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureAll","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_531":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureStrictControl_2133741","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_573":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureRightOne","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_622":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testAnd","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_658":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCapture2_2446744","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_668":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureFirst","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_778":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureAll","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_802":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testPrimitiveVsObject","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_812":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCapture1_2446744","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_822":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureNone","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_863":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureLast","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_866":{"methods":[{"sl":36},{"sl":40},{"sl":44},{"sl":51}],"name":"test","pass":true,"statements":[{"sl":37},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_869":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testPrimitive","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_947":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureNonStrictControl_2133741","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_964":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCapture1_2446744","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]},"test_979":{"methods":[{"sl":32},{"sl":40},{"sl":44},{"sl":51}],"name":"testCaptureRightOne","pass":true,"statements":[{"sl":33},{"sl":41},{"sl":46},{"sl":48},{"sl":52}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [430, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 212], [430, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 212], [], [], [866, 449], [866, 449], [], [], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [], [], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [], [], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [430, 866, 658, 863, 573, 778, 947, 211, 222, 869, 196, 668, 812, 979, 240, 802, 141, 531, 209, 436, 822, 622, 964, 190, 305, 453, 449, 212], [], []]
