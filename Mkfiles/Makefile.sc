# Makefile for the Netwide Assembler under 32-bit DOS(tm)
#
# The Netwide Assembler is copyright (C) 1996 Simon Tatham and
# Julian Hall. All rights reserved. The software is
# redistributable under the licence given in the file "Licence"
# distributed in the NASM archive.
#
# This Makefile is designed to build NASM using the 32-bit WIN32 C
# compiler Symantec(tm) C++ 7.5, provided you have a MAKE-utility
# that's compatible to SMAKE.

CC = sc
CCFLAGS = -c -a1 -mx -Nc -w2 -w7 -o+time -5
# -5            optimize for pentium (tm)
# -c            compile only
# -o-all        no optimizations (to avoid problems in disasm.c)
# -o+time       optimize for speed
# -o+space      optimize for size
# -A1           byte alignment for structures
# -mn           compile for Win32 executable
# -mx           compile for DOS386 (DOSX) executable
# -Nc           create COMDAT records
# -w2           possible unattended assignment: off
# -w7           for loops with empty instruction-body

LINK = link
LINKFLAGS = /noi /exet:DOSX
# /noignorecase all symbols are case-sensitive
# /exet:NT      Exetype: NT (Win32)
# /exet:DOSX    Exetype: DOSX (DOS32)
# /su:console   Subsystem: Console (Console-App)

LIBRARIES =
EXE = .exe
OBJ = obj

.c.$(OBJ):
        $(CC) $(CCFLAGS) $*.c


#
# modules needed for different programs
#

NASMOBJS = nasm.$(OBJ) nasmlib.$(OBJ) float.$(OBJ) insnsa.$(OBJ) \
           assemble.$(OBJ) labels.$(OBJ) parser.$(OBJ) outform.$(OBJ) \
	   output/outbin.$(OBJ) output/outaout.$(OBJ) output/outcoff.$(OBJ) output/outelf.$(OBJ) \
	   output/outobj.$(OBJ) output/outas86.$(OBJ) output/outrdf.$(OBJ) output/outrdf2.$(OBJ) \
	   output/outieee.$(OBJ) output/outdbg.$(OBJ) \
	   preproc.$(OBJ) listing.$(OBJ) eval.$(OBJ)

NDISASMOBJS = ndisasm.$(OBJ) disasm.$(OBJ) sync.$(OBJ) nasmlib.$(OBJ) \
              insnsd.$(OBJ)


#
# programs to create
#

all : nasm$(EXE) ndisasm$(EXE)


#
# We have to have a horrible kludge here to get round the 128 character
# limit, as usual... we'll simply use LNK-files :)
#
nasm$(EXE): $(NASMOBJS)
        $(LINK) $(LINKFLAGS) @<<
cx.obj $(NASMOBJS)
nasm.exe
<<

ndisasm$(EXE): $(NDISASMOBJS)
        $(LINK) $(LINKFLAGS) @<<
cx.obj $(NDISASMOBJS)
ndisasm.exe
<<



#
# modules for programs
#

disasm.$(OBJ): disasm.c nasm.h version.h insnsi.h disasm.h sync.h insns.h names.c insnsn.c
assemble.$(OBJ): assemble.c nasm.h version.h insnsi.h assemble.h insns.h
eval.$(OBJ): eval.c nasm.h version.h insnsi.h nasmlib.h eval.h
float.$(OBJ): float.c nasm.h version.h insnsi.h
labels.$(OBJ): labels.c nasm.h version.h insnsi.h nasmlib.h
listing.$(OBJ): listing.c nasm.h version.h insnsi.h nasmlib.h listing.h
nasm.$(OBJ): nasm.c nasm.h version.h insnsi.h nasmlib.h parser.h assemble.h labels.h \
	listing.h outform.h
nasmlib.$(OBJ): nasmlib.c nasm.h version.h insnsi.h nasmlib.h names.c insnsn.c
ndisasm.$(OBJ): ndisasm.c nasm.h version.h insnsi.h sync.h disasm.h
output/outas86.$(OBJ): output/outas86.c nasm.h version.h insnsi.h nasmlib.h
output/outaout.$(OBJ): output/outaout.c nasm.h version.h insnsi.h nasmlib.h
output/outbin.$(OBJ): output/outbin.c nasm.h version.h insnsi.h nasmlib.h
output/outcoff.$(OBJ): output/outcoff.c nasm.h version.h insnsi.h nasmlib.h
output/outdbg.$(OBJ): output/outdbg.c nasm.h version.h insnsi.h nasmlib.h
output/outelf.$(OBJ): output/outelf.c nasm.h version.h insnsi.h nasmlib.h
output/outobj.$(OBJ): output/outobj.c nasm.h version.h insnsi.h nasmlib.h
output/outrdf2.$(OBJ): output/outrdf2.c nasm.h version.h insnsi.h nasmlib.h
output/outieee.$(OBJ): output/outieee.c nasm.h version.h insnsi.h nasmlib.h
outform.$(OBJ): outform.c outform.h nasm.h version.h insnsi.h
parser.$(OBJ): parser.c nasm.h version.h insnsi.h nasmlib.h parser.h float.h names.c insnsn.c
preproc.$(OBJ): preproc.c macros.c preproc.h nasm.h version.h insnsi.h nasmlib.h
sync.$(OBJ): sync.c sync.h
insnsa.$(OBJ): insnsa.c nasm.h version.h insnsi.h insns.h
insnsd.$(OBJ): insnsd.c nasm.h version.h insnsi.h insns.h



clean :
	del *.obj
	del nasm$(EXE)
	del ndisasm$(EXE)
