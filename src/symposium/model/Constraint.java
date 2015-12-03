package symposium.model;


public interface Constraint {
}

class VenueConstraint implements Constraint {}
class SizeConstraint implements Constraint {}
class PairedPanalelistConstraint implements Constraint {}

abstract class TimeConstraint implements Constraint {}
class NewPanelistConstraint extends TimeConstraint {}
class DurationConstraint extends TimeConstraint {}

abstract class NoOverlapConstraint implements Constraint {}
class PanelistConstraint extends NoOverlapConstraint {}
class CategoryConstraint extends NoOverlapConstraint {}