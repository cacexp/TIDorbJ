//
// AuthenticationMethodListHolder.java (holder)
//
// File generated: Thu May 19 07:31:45 CEST 2011
//   by TIDorb idl2java 1.3.12
//

package org.omg.Security;

final public class AuthenticationMethodListHolder
   implements org.omg.CORBA.portable.Streamable {

  public int[] value; 
  public AuthenticationMethodListHolder() {
    value = new int[0];
  }

  public AuthenticationMethodListHolder(int[] initial) {
    value = initial;
  }

  public void _read(org.omg.CORBA.portable.InputStream is) {
    value = org.omg.Security.AuthenticationMethodListHelper.read(is);
  };

  public void _write(org.omg.CORBA.portable.OutputStream os) {
    org.omg.Security.AuthenticationMethodListHelper.write(os, value);
  };

  public org.omg.CORBA.TypeCode _type() {
    return org.omg.Security.AuthenticationMethodListHelper.type();
  };

}
