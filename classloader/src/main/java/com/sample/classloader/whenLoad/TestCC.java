package com.sample.classloader.whenLoad;

/**
 * @author yunfei.jyf
 * @date 2024/6/24
 */
public class TestCC {
    private TestBB testBB = new TestBB();

    public TestBB getTestBB() {
        return testBB;
    }

    public void setTestBB(TestBB testBB) {
        this.testBB = testBB;
    }
}
