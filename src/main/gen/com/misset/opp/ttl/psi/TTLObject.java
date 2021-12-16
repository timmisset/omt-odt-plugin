// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi;

import com.misset.opp.ttl.psi.spo.TTLSPO;
import com.misset.opp.ttl.stubs.object.TTLObjectStub;
import org.jetbrains.annotations.Nullable;

public interface TTLObject extends TTLSPO<TTLObjectStub> {

  @Nullable
  TTLBlankNode getBlankNode();

  @Nullable
  TTLBlankNodePropertyList getBlankNodePropertyList();

  @Nullable
  TTLCollection getCollection();

  @Nullable
  TTLIri getIri();

  @Nullable
  TTLLiteral getLiteral();

}
