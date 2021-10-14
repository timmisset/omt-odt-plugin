IntelliJ Plugin for OMT / ODT language

> Work in progress

This is a complete make-over of the current plugin.
- The OMT language will only ever be considered a YAML extension and is handled by extending the available YAML API available in IntelliJ
- The ODT language will be considered a plugin language that is analysed as inserted snippets
  - References to the OMT language and other ODT snippets will be possible
  - ...
- The TTL (Turtle) language will be copied as is, this is mainly used to generate the Apache Jena model that is used to traverse queries

The YAML API supports an experimental feature called a MetaType provider. This has been implemented in this plugin which appears to be stable 
and reduced the amount of code for this plugin significantly since it is facilitates Yaml/OMT path traversing and translation into
OMT elements that can than be easily plugged into Inspections.
The plugin now contains many marker annotations for Unstable that are inherited from the Yaml API
