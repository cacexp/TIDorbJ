//
// EncodingHolder.java (holder)
//
// File generated: Thu May 19 07:31:38 CEST 2011
//   by TIDorb idl2java 1.3.12
//

package org.omg.IOP;

final public class EncodingHolder
   implements org.omg.CORBA.portable.Streamable {

  public Encoding value; 
  public EncodingHolder() {
  }

  public EncodingHolder(Encoding initial) {
    value = initial;
  }

  public void _read(org.omg.CORBA.portable.InputStream is) {
    value = org.omg.IOP.EncodingHelper.read(is);
  };

  public void _write(org.omg.CORBA.portable.OutputStream os) {
    org.omg.IOP.EncodingHelper.write(os, value);
  };

  public org.omg.CORBA.TypeCode _type() {
    return org.omg.IOP.EncodingHelper.type();
  };

}
