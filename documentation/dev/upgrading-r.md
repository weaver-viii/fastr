# Notes for upgrading R version

Files where to update the version:
* `mx.fastr/suite.py` ("GNUR" library)
* `mx.fastr/mx_fastr.py` (function `r_version`)
* `com.oracle.truffle.r.native/Makefile` (variable `R_VERSION`)
* `README.md`
* `RVersionNumber` (use `R.Version()`, also update `INTERNALS_UID` from `Rinternals.h`)

Updating GNU-R sources patched by FastR (make sure you have up to date `gnur` and `master` branches):
```
R_VERSION=3.5.1
git checkout gnur
cd com.oracle.truffle.r.native/gnur/patch/src
for f in `find . -type f`; do cp {GNUR_SOURCES}/src/$f $f; done
git commit -m "Upgrading to R-$R_VERSION"
git checkout master
git checkout -b "upgrade-r-$R_VERSION"
git merge gnur
```
now solve all the merge conflicts. Note that file `gramRd.c` contains generated
code and the automatic merge seems to do a very bad job at merging that,
so it is recommended to take a look at all the changes introduced in `master`
(will be few commits) and manually redo them.

There are native functions ported from GNU-R source to Java.
With each upgrade we should review the ported code and port any
relevant fixes and improvements.

File `share/R/examples-footer.R` may occasionally change.
FastR copy is in `com.oracle.truffle.r.native/run/examples-footer.R`.

File `com.oracle.truffle.r.native/run/configure.ac` is taken from GNU-R and modified.
See the comments inside the FastR version of that file.

Tips:

* run `mx rbcheck` and compare to output of `mx rbcheck` in FastR with older R version
* enable `NativeMirror#TRACE_MIRROR_ALLOCATION_SITES` to debug issues in `gramRd.c`
* run unit tests with `AddIgnoreForFailedTests=true` to make the newly failing tests
ignored with `Ignored.NewRVersionMigration`, e.g. `mx --J @'-DAddIgnoreForFailedTests=true' rutgen`.
