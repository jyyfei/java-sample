package com.sample.classloader.defaultAccess;

/**
 * @author yunfei.jyf
 * @date 2024/4/16
 */
public class AClass {
    private BClass bClass;

    public AClass() {
        bClass = new BClass();
    }
}
