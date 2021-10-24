The OMT MetaProvider contains the entire OMT Yaml structure with all type definitions.

Based on the Yaml path a specific OMT MetaType is provided for a Yaml structure  
For example
```yaml
model:
  MyActivity: !Activity
    title: A Title
```
The value (YamlMapping) for model is resolved to an OMTModelItem, which in turn uses the
tag !Activity to determine which kind of OMTModelItem is used. Using a delegate structure
the OMTActivity is then provided which contains a field title, with an OMTMetaType InterpolatedString

The MetaTypes are used all over the OMT part of the plugin to introduce specific validations,
determine where to inject ODT language and more.

> The MetaProvider is extended from the Yaml MetaTypeProvider included in the YamlPlugin of IntelliJ
