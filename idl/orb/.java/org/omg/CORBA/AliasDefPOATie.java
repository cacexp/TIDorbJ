//
// AliasDefPOATie.java (tie)
//
// File generated: Thu May 19 07:31:32 CEST 2011
//   by TIDorb idl2java 1.3.12
//

package org.omg.CORBA;

public class AliasDefPOATie
 extends AliasDefPOA
 implements AliasDefOperations {

  private AliasDefOperations _delegate;
  public AliasDefPOATie(AliasDefOperations delegate) {
    this._delegate = delegate;
  };

  public AliasDefOperations _delegate() {
    return this._delegate;
  };

  public java.lang.String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectID) {
    return __ids;
  };

  private static java.lang.String[] __ids = {
    "IDL:omg.org/CORBA/AliasDef:1.0",
    "IDL:omg.org/CORBA/TypedefDef:1.0",
    "IDL:omg.org/CORBA/Contained:1.0",
    "IDL:omg.org/CORBA/IRObject:1.0",
    "IDL:omg.org/CORBA/IDLType:1.0"  };

  public org.omg.CORBA.IDLType original_type_def() {
    return this._delegate.original_type_def();
  }

  public void original_type_def(org.omg.CORBA.IDLType value) {
    this._delegate.original_type_def(value);
  }

  public java.lang.String id() {
    return this._delegate.id();
  }

  public void id(java.lang.String value) {
    this._delegate.id(value);
  }

  public java.lang.String name() {
    return this._delegate.name();
  }

  public void name(java.lang.String value) {
    this._delegate.name(value);
  }

  public java.lang.String version() {
    return this._delegate.version();
  }

  public void version(java.lang.String value) {
    this._delegate.version(value);
  }

  public org.omg.CORBA.Container defined_in() {
    return this._delegate.defined_in();
  }

  public java.lang.String absolute_name() {
    return this._delegate.absolute_name();
  }

  public org.omg.CORBA.Repository containing_repository() {
    return this._delegate.containing_repository();
  }

  public org.omg.CORBA.ContainedPackage.Description describe() {
    return this._delegate.describe(
    );
  };

  public void move(org.omg.CORBA.Container new_container, java.lang.String new_name, java.lang.String new_version) {
    this._delegate.move(
    new_container, 
    new_name, 
    new_version
    );
  };

  public org.omg.CORBA.DefinitionKind def_kind() {
    return this._delegate.def_kind();
  }

  public void destroy() {
    this._delegate.destroy(
    );
  };



  public org.omg.CORBA.TypeCode type() {
    return this._delegate.type();
  }




}
