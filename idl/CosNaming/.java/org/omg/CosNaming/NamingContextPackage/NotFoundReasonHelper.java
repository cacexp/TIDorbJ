//
// NotFoundReasonHelper.java (helper)
//
// File generated: Thu May 19 07:31:39 CEST 2011
//   by TIDorb idl2java 1.3.12
//

package org.omg.CosNaming.NamingContextPackage;

abstract public class NotFoundReasonHelper {

  private static org.omg.CORBA.ORB _orb() {
    return org.omg.CORBA.ORB.init();
  }

  public static void insert(org.omg.CORBA.Any any, NotFoundReason value) {
    any.insert_Streamable(new NotFoundReasonHolder(value));
  };

  public static NotFoundReason extract(org.omg.CORBA.Any any) {
    if(any instanceof es.tid.CORBA.Any) {
      try {
        org.omg.CORBA.portable.Streamable holder =
          ((es.tid.CORBA.Any)any).extract_Streamable();
        if(holder instanceof NotFoundReasonHolder){
          return ((NotFoundReasonHolder) holder).value;
        }
      } catch (Exception e) {}
    }

    return read(any.create_input_stream());
  };

  private static org.omg.CORBA.TypeCode _type = null;
  public static org.omg.CORBA.TypeCode type() {
    if (_type == null) {
      java.lang.String[] members = new java.lang.String[3];
      members[0] = "missing_node";
      members[1] = "not_context";
      members[2] = "not_object";
      _type = _orb().create_enum_tc(id(), "NotFoundReason", members);
    }
    return _type;
  };

  public static String id() {
    return "IDL:omg.org/CosNaming/NamingContext/NotFoundReason:1.0";
  };

  public static NotFoundReason read(org.omg.CORBA.portable.InputStream is) {
    return NotFoundReason.from_int(is.read_long());
  };

  public static void write(org.omg.CORBA.portable.OutputStream os, NotFoundReason val) {
    os.write_long(val.value());
  };

}
