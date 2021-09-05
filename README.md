FeedbackVectors
===

**THIS MOD IS EXPERIMENTAL, IT MAY RESULT IN RACE CONDITIONS WHICH DRIVE UNEXPECTED BEHAVIOUR.**

FeedbackVectors is a performance and API mod for galimulator.
Performance mod because it remove the thread-safety aspect of java.util.Vector as well as backing them to a HashSet
and if applicable to a HashMap<Integer, T> (if T supports having UIDs) this drastically increases performance
of operations like #contains.

The API aspect of the mod is done by making the vectors call consumers if the vector is altered.

Dependencies:
 - Starloader (any relatively modern version should suffice)
 - StarloaderAPI 1.5.0 or newer (as of now unreleased, but that will change in the future)
