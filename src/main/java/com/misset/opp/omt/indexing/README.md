## Indexing in OMT

The Stub Indexing in OMT cannot be used for the injected languages. Therefor it isn't a very usable feature since the
most important part of the indexing is to keep track of the which exportable members are available. These are in part
made available from the injected languages itself.

Imports / Exports in OMT itself are also all unspecified making it difficult to process them quickly when analysing
them.

For example:

```yaml
import:
  ./fileA.omt:
    - memberA

commands: |
  DEFINE COMMAND memberB => {}

model:
  memberC: !Activity
    commands: |
      DEFINE COMMAND memberD => {}
```

memberA is imported but as such is also available for export memberB is declared in injected fragment in the root of the
file and is available for export memberC is declared in the OMT model and is available for export memberD is declared in
the OMT model item and us NOT available for export

For the language plugin, memberA is a YamlSequenceItem and memberC a YamlKeyValue, both part of the OMT/Yaml language.
MemberB and memberD are both part of the ODT language which is not indexed when part of an injected fragment.

There are a few routes to determine which exports are available in a file:

- Checking if an imported member is available, i.e. is memberA an exportable member of fileA.omt
- Checking if a local call can be made, i.e. memberA() is available as command
- Resolving a call to memberA, i.e. retrieving the Callable counterpart of the call to retrieve expected argument etc
