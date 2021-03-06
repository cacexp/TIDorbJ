/***** Copyright (c) 2001 Object Management Group. Unlimited rights to 
       duplicate and use this code are hereby granted provided that this 
       copyright notice is included.
*****/

package org.omg.CORBA;

public class TIMEOUT extends org.omg.CORBA.SystemException {

    public TIMEOUT() {
        super(null, 0, CompletionStatus.COMPLETED_NO);
    }

    public TIMEOUT(int minor, CompletionStatus completed) {
        super(null, minor, completed);
    }

    public TIMEOUT(String reason) {
        super(reason, 0, CompletionStatus.COMPLETED_NO);
    }

    public TIMEOUT(String reason, int minor, CompletionStatus completed) {
        super(reason, minor, completed);
    }

}
