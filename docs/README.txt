July 21, 2014

IEEEtran.cls version 1.8-(modification Prajit) revises format specifications so that the final
layout more closely matches the layout of the Microsoft Word templates.

Additionally, the IEEEtran_HOWTO.pdf now includes the notice to not use
symbols, special characters, or math in the paper title and abstract.


*******


March 5, 2007


IEEEtran is the official LaTeX class for authors of the Institute of
Electrical and Electronics Engineers (IEEE) transactions journals and
conferences. The latest version of the IEEEtran package can be found
at CTAN:

http://www.ctan.org/tex-archive/macros/latex/contrib/IEEEtran/

as well as within IEEE's site:

http://www.ieee.org/

For latest news, helpful tips, answers to frequently asked questions,
beta releases and other support, visit the IEEEtran home page at my
website:  http://www.michaelshell.org/tex/ieeetran/


Version 1.7a is a bug fix release that corrects the two column peer
review title page problem. This problem was not present in the 1.6 series.


V1.7 is a significant update over the 1.6 series with many important
changes. For a full list, please read the file changelog.txt. The most
notable changes include:

 1. New class option compsoc to support the IEEE Computer Society format.

 2. Several commands and environments have been deprecated in favor of
    replacements with IEEE prefixes to better avoid potential future name
    clashes with other packages. Legacy code retained to allow the use of
    the obsolete forms (for now), but with a warning message to the console
    during compilation:
    \IEEEauthorblockA, \IEEEauthorblockN, \IEEEauthorrefmark,
    \IEEEbiography, \IEEEbiographynophoto, \IEEEkeywords, \IEEEPARstart,
    \IEEEproof, \IEEEpubid, \IEEEpubidadjcol, \IEEEQED, \IEEEQEDclosed,
    \IEEEQEDopen, \IEEEspecialpapernotice. IEEEtran.cls now redefines
    \proof in way to avoid problems with the amsthm.sty package.
    For IED lists:
    \IEEEiedlabeljustifyc, \IEEEiedlabeljustifyl, \IEEEiedlabeljustifyr,
    \IEEEnocalcleftmargin, \IEEElabelindent, \IEEEsetlabelwidth,
    \IEEEusemathlabelsep
    These commands/lengths now require the IEEE prefix and do not have
    legacy support: \IEEEnormaljot.
    For IED lists: \ifIEEEnocalcleftmargin, \ifIEEEnolabelindentfactor,
    \IEEEiedlistdecl, \IEEElabelindentfactor

 3. New \CLASSINPUT, \CLASSOPTION and \CLASSINFO interface allows for more
    user control and conditional compilation.

 4. Several bug fixes and improved compatibility with other packages.


A note to those who create classes derived from IEEEtran.cls: Consider the
use of patch code, either in an example .tex file or as a .sty file,
rather than creating a new class. The IEEEtran.cls CLASSINPUT interface
allows IEEEtran.cls to be fully programmable with respect to document
margins, so there is no need for new class files just for altered margins.
In this way, authors can benefit from updates to IEEEtran.cls and the need
to maintain derivative classes and backport later IEEEtran.cls revisions
thereto is avoided. As always, developers who create classes derived from
IEEEtran.cls should use a different name for the derived class, so that it
cannot be confused with the official/base version here, as well as provide
authors with technical support for the derived class. It is generally a bad
idea to produce a new class that is not going to be maintained.


Best wishes for all your publication endeavors,

Michael Shell
http://www.michaelshell.org/


********************************** Files **********************************

README                 - This file.

IEEEtran.cls           - The IEEEtran LaTeX class file.

changelog.txt          - The revision history.

IEEEtran_HOWTO.pdf     - The IEEEtran LaTeX class user manual.

bare_conf.tex          - A bare bones starter file for conference papers.

***************************************************************************
Legal Notice:
This code is offered as-is without any warranty either expressed or
implied; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE! 
User assumes all risk.
In no event shall IEEE or any contributor to this code be liable for
any damages or losses, including, but not limited to, incidental,
consequential, or any other damages, resulting from the use or misuse
of any information contained here.

All comments are the opinions of their respective authors and are not
necessarily endorsed by the IEEE.

This work is distributed under the LaTeX Project Public License (LPPL)
( http://www.latex-project.org/ ) version 1.3, and may be freely used,
distributed and modified. A copy of the LPPL, version 1.3, is included
in the base LaTeX documentation of all distributions of LaTeX released
2003/12/01 or later.
Retain all contribution notices and credits.
** Modified files should be clearly indicated as such, including  **
** renaming them and changing author support contact information. **

File list of work: IEEEtran.cls, IEEEtran_HOWTO.pdf, bare_conf.tex
***************************************************************************


January 11, 2007


IEEEtran.bst is the official BibTeX style for authors of the Institute of
Electrical and Electronics Engineers (IEEE) Transactions journals and
conferences.

It also may have applications for other academic work such as theses and
technical reports. The alphanumeric and natbib variants extend the
applicability of the IEEEtran bibstyle family to the natural sciences
and beyond.

The IEEEtran bibstyle is a very comprehensive BibTeX style which provides
many features beyond the standard BibTeX styles, including full support
for references of online documents, patents, periodicals and standards.
See the provided user manual for detailed usage information.

The latest version of the IEEEtran BibTeX style can be found at CTAN:

http://www.ctan.org/tex-archive/macros/latex/contrib/IEEEtran/bibtex/

as well as within IEEE's site:

http://www.ieee.org/

Note that the packages at IEEE's site do not contain the natbib and
alphanumeric variants (e.g., IEEEtranN.bst, etc.) as these are not used
for IEEE related work. These files can be obtained on CTAN.

For helpful tips, answers to frequently asked questions and other support,
visit the IEEEtran home page at my website:

http://www.michaelshell.org/tex/ieeetran/


Enjoy!

Michael Shell
http://www.michaelshell.org/

*******
Version 1.12 (2007/01/11) changes:

 1. Fixed bug with unwanted comma before "et al." when an entry contained
    more than two author names. Thanks to Pallav Gupta for reporting this.

 2. Fixed bug with anomalous closing quote in tech reports that have a
    type, but without a number or address. Thanks to Mehrdad Mirreza for
    reporting this.

 3. Use braces in \providecommand in begin.bib to better support
    latex2html. TeX style length assignments OK with recent versions
    of latex2html - 1.71 (2002/2/1) or later is strongly recommended.
    Use of the language field still causes trouble with latex2html.
    Thanks to Federico Beffa for reporting this.

 4. Added IEEEtran.bst ID and version comment string to .bbl output.

 5. Provide a \BIBdecl hook that allows the user to execute commands
    just prior to the first entry.

 6. Use default urlstyle (is using url.sty) of "same" rather than rm to
    better work with a wider variety of bibliography styles.

 7. Changed month abbreviations from Sept., July and June to Sep., Jul.,
    and Jun., respectively, as IEEE now does. Thanks to Moritz Borgmann
    for reporting this.

 8. Control entry types should not be considered when calculating longest
    label width.

 9. Added alias www for electronic/online.

10. Updated full and abbreviated journal name string definitions in
    IEEEfull.bib and IEEEabrv.bib.

11. New IEEEtranSA.bst, IEEEtranN.bst, and IEEEtranSN.bst variants for
    alphanumeric citation tags and natbib compatibility.


********************************** Files **********************************

README                 - This file.

IEEEtran_bst_HOWTO.pdf - The user manual.

IEEEtran.bst           - The standard IEEEtran BibTeX style file. For use
                         with IEEE work.

IEEEtranS.bst          - A version of IEEEtran.bst that sorts the entries.
                         Some IEEE conferences/publications may use/allow
                         sorted bibliographies.

IEEEexample.bib        - An example BibTeX database that contains the
                         references shown in the user manual.

IEEEabrv.bib           - String definitions for the abbreviated names of
                         IEEE journals. (For use with IEEE work.)
                         
IEEEfull.bib           - String definitions for the full names of IEEE
                         journals. (Do not use for IEEE work.)


Carried on CTAN only, for non-IEEE related work:

IEEEtranSA.bst         - Like IEEEtranS.bst, but with alphanumeric citation
                         tags like alpha.bst. Not for normal IEEE use.

EEEtranN.bst          - Like IEEEtran.bst, but based on plainnat.bst and
                         is compatible with Patrick W. Daly's natbib
                         package. Not for normal IEEE use.

IEEEtranSN.bst         - Sorting version of IEEEtranN.bst. Not for normal
                         IEEE use

***************************************************************************
Legal Notice:
This code is offered as-is without any warranty either expressed or
implied; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE! 
User assumes all risk.
In no event shall IEEE or any contributor to this code be liable for
any damages or losses, including, but not limited to, incidental,
consequential, or any other damages, resulting from the use or misuse
of any information contained here.

All comments are the opinions of their respective authors and are not
necessarily endorsed by the IEEE.

This work is distributed under the LaTeX Project Public License (LPPL)
( http://www.latex-project.org/ ) version 1.3, and may be freely used,
distributed and modified. A copy of the LPPL, version 1.3, is included
in the base LaTeX documentation of all distributions of LaTeX released
2003/12/01 or later.
Retain all contribution notices and credits.
** Modified files should be clearly indicated as such, including  **
** renaming them and changing author support contact information. **

File list of work: IEEEtran_bst_HOWTO.pdf, IEEEtran.bst, IEEEtranS.bst,
                   IEEEtranSA.bst, IEEEtranN.bst, IEEEtranSN.bst,
                   IEEEexample.bib, IEEEabrv.bib, IEEEfull.bib
                   
***************************************************************************
