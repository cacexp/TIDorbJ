//
// IdAssignmentPolicyValueHolder.java (holder)
//
// File generated: Thu May 19 07:31:32 CEST 2011
//   by TIDorb idl2java 1.3.12
//

package org.omg.PortableServer;

final public class IdAssignmentPolicyValueHolder
   implements org.omg.CORBA.portable.Streamable {

  public IdAssignmentPolicyValue value; 
  public IdAssignmentPolicyValueHolder() {
  }

  public IdAssignmentPolicyValueHolder(IdAssignmentPolicyValue initial) {
    value = initial;
  }

  public void _read(org.omg.CORBA.portable.InputStream is) {
    value = org.omg.PortableServer.IdAssignmentPolicyValueHelper.read(is);
  };

  public void _write(org.omg.CORBA.portable.OutputStream os) {
    org.omg.PortableServer.IdAssignmentPolicyValueHelper.write(os, value);
  };

  public org.omg.CORBA.TypeCode _type() {
    return org.omg.PortableServer.IdAssignmentPolicyValueHelper.type();
  };

}