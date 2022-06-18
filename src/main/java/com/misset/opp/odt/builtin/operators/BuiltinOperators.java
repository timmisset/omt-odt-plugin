package com.misset.opp.odt.builtin.operators;

import com.misset.opp.resolvable.Callable;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BuiltinOperators {

    private static final HashMap<String, Callable> builtinOperators = new HashMap<>();
    static void addOperator(BuiltInOperator operator) {
        builtinOperators.put(operator.getCallId(), operator);
    }
    static {
        addOperator(AndOperator.INSTANCE);
        addOperator(BlankNodeOperator.INSTANCE);
        addOperator(CastOperator.INSTANCE);
        addOperator(CatchOperator.INSTANCE);
        addOperator(CeilOperator.INSTANCE);
        addOperator(ConcatOperator.INSTANCE);
        addOperator(ContainsOperator.INSTANCE);
        addOperator(CountOperator.INSTANCE);
        addOperator(CurrentDateOperator.INSTANCE);
        addOperator(CurrentDateTimeOperator.INSTANCE);
        addOperator(DistinctOperator.INSTANCE);
        addOperator(DivideByOperator.INSTANCE);
        addOperator(DurationOperator.INSTANCE);
        addOperator(ElementsOperator.INSTANCE);
        addOperator(EmptyOperator.INSTANCE);
        addOperator(EqualsOperator.INSTANCE);
        addOperator(ErrorOperator.INSTANCE);
        addOperator(EveryOperator.INSTANCE);
        addOperator(ExistsOperator.INSTANCE);
        addOperator(FilterOperator.INSTANCE);
        addOperator(FindSubjectsOperator.INSTANCE);
        addOperator(FirstOperator.INSTANCE);
        addOperator(FloorOperator.INSTANCE);
        addOperator(DateFormatOperator.INSTANCE);
        addOperator(FormatOperator.INSTANCE);
        addOperator(GraphOperator.INSTANCE);
        addOperator(GreaterThanEqualsOperator.INSTANCE);
        addOperator(GreaterThanOperator.INSTANCE);
        addOperator(HasOperator.INSTANCE);
        addOperator(IdentityOperator.INSTANCE);
        addOperator(IfEmptyOperator.INSTANCE);
        addOperator(IIfOperator.INSTANCE);
        addOperator(IndexOfOperator.INSTANCE);
        addOperator(InOperator.INSTANCE);
        addOperator(IriOperator.INSTANCE);
        addOperator(JoinOperator.INSTANCE);
        addOperator(JsonOperator.INSTANCE);
        addOperator(LastOperator.INSTANCE);
        addOperator(LengthOperator.INSTANCE);
        addOperator(LessThanEqualsOperator.INSTANCE);
        addOperator(LessThanOperator.INSTANCE);
        addOperator(LogOperator.INSTANCE);
        addOperator(LowerCaseOperator.INSTANCE);
        addOperator(MapOperator.INSTANCE);
        addOperator(MaxOperator.INSTANCE);
        addOperator(MergeOperator.INSTANCE);
        addOperator(MinOperator.INSTANCE);
        addOperator(MinusOperator.INSTANCE);
        addOperator(NotOperator.INSTANCE);
        addOperator(OrderByOperator.INSTANCE);
        addOperator(OrOperator.INSTANCE);
        addOperator(PickOperator.INSTANCE);
        addOperator(PlusOperator.INSTANCE);
        addOperator(RepeatOperator.INSTANCE);
        addOperator(ReplaceOperator.INSTANCE);
        addOperator(ReverseOperator.INSTANCE);
        addOperator(RoundOperator.INSTANCE);
        addOperator(SkipOperator.INSTANCE);
        addOperator(SomeOperator.INSTANCE);
        addOperator(SplitOperator.INSTANCE);
        addOperator(SubstringOperator.INSTANCE);
        addOperator(SumOperator.INSTANCE);
        addOperator(TakeOperator.INSTANCE);
        addOperator(TimesOperator.INSTANCE);
        addOperator(TraverseOperator.INSTANCE);
        addOperator(TrimOperator.INSTANCE);
        addOperator(TruncOperator.INSTANCE);
        addOperator(TypeOperator.INSTANCE);
        addOperator(UpperCaseOperator.INSTANCE);
    }

    public static Collection<Callable> getStaticOperators() {
        return builtinOperators.values().stream().filter(Callable::isStatic).collect(Collectors.toList());
    }

    public static Collection<Callable> getNonStaticOperators() {
        return builtinOperators.values().stream().filter(callable -> !callable.isStatic()).collect(Collectors.toList());
    }

    public static Collection<Callable> getOperators() {
        return builtinOperators.values();
    }

    public static Callable get(String name) {
        return builtinOperators.get(name);
    }
}
