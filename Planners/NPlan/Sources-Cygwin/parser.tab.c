/* A Bison parser, made by GNU Bison 3.0.4.  */

/* Bison implementation for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "3.0.4"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1




/* Copy the first part of user declarations.  */
#line 4 "parser.y" /* yacc.c:339  */


#include "stdio.h"
#include "main.h"
#include "asyntax.h"

void rparen(char *);

int COST;


#line 78 "parser.tab.c" /* yacc.c:339  */

# ifndef YY_NULLPTR
#  if defined __cplusplus && 201103L <= __cplusplus
#   define YY_NULLPTR nullptr
#  else
#   define YY_NULLPTR 0
#  endif
# endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* In a future release of Bison, this section will be replaced
   by #include "parser.tab.h".  */
#ifndef YY_YY_PARSER_TAB_H_INCLUDED
# define YY_YY_PARSER_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token type.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    RPAREN = 258,
    LPAREN = 259,
    DASH = 260,
    rwDEFINE = 261,
    rwACTION = 262,
    rwPARAMS = 263,
    rwEFFECT = 264,
    rwPRECOND = 265,
    rwPREDICATES = 266,
    rwREQUIREMENTS = 267,
    rwTYPES = 268,
    rwOBJECTS = 269,
    rwINIT = 270,
    rwGOAL = 271,
    rwDOMAIN = 272,
    rwTYPING = 273,
    rwAND = 274,
    rwOR = 275,
    rwWHEN = 276,
    rwNOT = 277,
    rwIMPLY = 278,
    rwFORALL = 279,
    rwPROBLEM = 280,
    EQUA = 281,
    rwEXISTS = 282,
    rwLENGTH = 283,
    rwCONSTANTS = 284,
    rwEITHER = 285,
    rwINCREASE = 286,
    rwMETRIC = 287,
    rwMINIMIZE = 288,
    ID = 289,
    VAR = 290,
    INT = 291,
    rwFUNCTIONS = 292
  };
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED

union YYSTYPE
{
#line 16 "parser.y" /* yacc.c:355  */

  int i;
  intlist *intlistp;
  atomlist *atomlistp;
  atom *atomp;
  Sfma *Sfmap;
  Sfmalist *Sfmalistp;
  Seff *Seffp;
  Sefflist *Sefflistp;
  typedvarlist *typedvarlistp;

#line 168 "parser.tab.c" /* yacc.c:355  */
};

typedef union YYSTYPE YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);

#endif /* !YY_YY_PARSER_TAB_H_INCLUDED  */

/* Copy the second part of user declarations.  */

#line 185 "parser.tab.c" /* yacc.c:358  */

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#else
typedef signed char yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(Msgid) dgettext ("bison-runtime", Msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(Msgid) Msgid
# endif
#endif

#ifndef YY_ATTRIBUTE
# if (defined __GNUC__                                               \
      && (2 < __GNUC__ || (__GNUC__ == 2 && 96 <= __GNUC_MINOR__)))  \
     || defined __SUNPRO_C && 0x5110 <= __SUNPRO_C
#  define YY_ATTRIBUTE(Spec) __attribute__(Spec)
# else
#  define YY_ATTRIBUTE(Spec) /* empty */
# endif
#endif

#ifndef YY_ATTRIBUTE_PURE
# define YY_ATTRIBUTE_PURE   YY_ATTRIBUTE ((__pure__))
#endif

#ifndef YY_ATTRIBUTE_UNUSED
# define YY_ATTRIBUTE_UNUSED YY_ATTRIBUTE ((__unused__))
#endif

#if !defined _Noreturn \
     && (!defined __STDC_VERSION__ || __STDC_VERSION__ < 201112)
# if defined _MSC_VER && 1200 <= _MSC_VER
#  define _Noreturn __declspec (noreturn)
# else
#  define _Noreturn YY_ATTRIBUTE ((__noreturn__))
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(E) ((void) (E))
#else
# define YYUSE(E) /* empty */
#endif

#if defined __GNUC__ && 407 <= __GNUC__ * 100 + __GNUC_MINOR__
/* Suppress an incorrect diagnostic about yylval being uninitialized.  */
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN \
    _Pragma ("GCC diagnostic push") \
    _Pragma ("GCC diagnostic ignored \"-Wuninitialized\"")\
    _Pragma ("GCC diagnostic ignored \"-Wmaybe-uninitialized\"")
# define YY_IGNORE_MAYBE_UNINITIALIZED_END \
    _Pragma ("GCC diagnostic pop")
#else
# define YY_INITIAL_VALUE(Value) Value
#endif
#ifndef YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_END
#endif
#ifndef YY_INITIAL_VALUE
# define YY_INITIAL_VALUE(Value) /* Nothing. */
#endif


#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined EXIT_SUCCESS
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
      /* Use EXIT_SUCCESS as a witness for stdlib.h.  */
#     ifndef EXIT_SUCCESS
#      define EXIT_SUCCESS 0
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's 'empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (0)
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined EXIT_SUCCESS \
       && ! ((defined YYMALLOC || defined malloc) \
             && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef EXIT_SUCCESS
#    define EXIT_SUCCESS 0
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined EXIT_SUCCESS
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined EXIT_SUCCESS
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
         || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

# define YYCOPY_NEEDED 1

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)                           \
    do                                                                  \
      {                                                                 \
        YYSIZE_T yynewbytes;                                            \
        YYCOPY (&yyptr->Stack_alloc, Stack, yysize);                    \
        Stack = &yyptr->Stack_alloc;                                    \
        yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
        yyptr += yynewbytes / sizeof (*yyptr);                          \
      }                                                                 \
    while (0)

#endif

#if defined YYCOPY_NEEDED && YYCOPY_NEEDED
/* Copy COUNT objects from SRC to DST.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(Dst, Src, Count) \
      __builtin_memcpy (Dst, Src, (Count) * sizeof (*(Src)))
#  else
#   define YYCOPY(Dst, Src, Count)              \
      do                                        \
        {                                       \
          YYSIZE_T yyi;                         \
          for (yyi = 0; yyi < (Count); yyi++)   \
            (Dst)[yyi] = (Src)[yyi];            \
        }                                       \
      while (0)
#  endif
# endif
#endif /* !YYCOPY_NEEDED */

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  5
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   175

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  38
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  64
/* YYNRULES -- Number of rules.  */
#define YYNRULES  112
/* YYNSTATES -- Number of states.  */
#define YYNSTATES  221

/* YYTRANSLATE[YYX] -- Symbol number corresponding to YYX as returned
   by yylex, with out-of-bounds checking.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   292

#define YYTRANSLATE(YYX)                                                \
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[TOKEN-NUM] -- Symbol number corresponding to TOKEN-NUM
   as returned by yylex, without out-of-bounds checking.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37
};

#if YYDEBUG
  /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
static const yytype_uint8 yyrline[] =
{
       0,    55,    55,    58,    59,    62,    65,    65,    68,    69,
      70,    73,    74,    77,    78,    83,    83,    83,    86,    87,
      90,    90,    91,    91,    92,    92,    93,    93,    94,    94,
      95,    95,    95,    98,    99,   102,   102,   103,   104,   107,
     108,   109,   112,   113,   116,   117,   120,   124,   124,   124,
     127,   127,   128,   128,   131,   132,   133,   134,   135,   139,
     140,   143,   144,   144,   145,   146,   149,   149,   150,   153,
     154,   157,   158,   161,   161,   166,   166,   167,   167,   168,
     168,   169,   169,   170,   170,   171,   171,   172,   173,   173,
     174,   174,   175,   175,   178,   179,   182,   183,   186,   187,
     190,   190,   191,   191,   192,   192,   193,   193,   194,   195,
     195,   196,   196
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || 0
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "RPAREN", "LPAREN", "DASH", "rwDEFINE",
  "rwACTION", "rwPARAMS", "rwEFFECT", "rwPRECOND", "rwPREDICATES",
  "rwREQUIREMENTS", "rwTYPES", "rwOBJECTS", "rwINIT", "rwGOAL", "rwDOMAIN",
  "rwTYPING", "rwAND", "rwOR", "rwWHEN", "rwNOT", "rwIMPLY", "rwFORALL",
  "rwPROBLEM", "EQUA", "rwEXISTS", "rwLENGTH", "rwCONSTANTS", "rwEITHER",
  "rwINCREASE", "rwMETRIC", "rwMINIMIZE", "ID", "VAR", "INT",
  "rwFUNCTIONS", "$accept", "begin", "idlist", "costexpr", "atom", "$@1",
  "atomlist", "varid", "varidlist", "domain", "$@2", "$@3", "domaindefs",
  "domaindef", "$@4", "$@5", "$@6", "$@7", "$@8", "$@9", "$@10", "actdefs",
  "actdef", "$@11", "opvars", "varlist", "opvarlist", "opvar", "problem",
  "$@12", "$@13", "defs", "$@14", "$@15", "def", "objectlist",
  "typedvarlist", "$@16", "typedatoms", "$@17", "functiondecls",
  "functiondecl", "tdecl", "$@18", "fma", "$@19", "$@20", "$@21", "$@22",
  "$@23", "$@24", "$@25", "$@26", "$@27", "fmas", "effects", "numexpr",
  "effect", "$@28", "$@29", "$@30", "$@31", "$@32", "$@33", YY_NULLPTR
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[NUM] -- (External) token number corresponding to the
   (internal) symbol number NUM (which must be that of a token).  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292
};
# endif

#define YYPACT_NINF -148

#define yypact_value_is_default(Yystate) \
  (!!((Yystate) == (-148)))

#define YYTABLE_NINF -1

#define yytable_value_is_error(Yytable_value) \
  0

  /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
     STATE-NUM.  */
static const yytype_int16 yypact[] =
{
      18,    23,    31,    33,    36,  -148,    39,  -148,    32,    46,
      19,    40,  -148,    26,    64,  -148,  -148,    69,    75,    76,
       7,    70,  -148,     9,    77,  -148,  -148,    34,    34,    34,
      78,  -148,    34,  -148,    86,    59,    61,  -148,     9,    92,
      62,    93,    34,  -148,    95,  -148,  -148,    68,  -148,    78,
      99,  -148,    94,    65,  -148,  -148,  -148,  -148,   103,  -148,
    -148,    54,    73,   105,  -148,   106,    79,   107,   108,    80,
     109,  -148,    82,     1,  -148,  -148,    86,    86,    86,    86,
      86,   110,   -18,   113,   -18,    94,  -148,   115,   116,   118,
      86,  -148,    54,    80,  -148,  -148,    34,  -148,  -148,    16,
    -148,  -148,  -148,     3,  -148,   120,    86,    86,    86,  -148,
      86,    84,  -148,  -148,   -18,    84,   -18,  -148,  -148,    84,
      35,  -148,  -148,  -148,   121,  -148,  -148,  -148,     8,  -148,
     122,    96,  -148,  -148,     3,  -148,  -148,   124,   128,  -148,
     130,  -148,    84,   131,   133,  -148,    84,  -148,   132,  -148,
     136,  -148,   118,    86,   137,   139,   137,  -148,   141,   117,
      80,  -148,   142,  -148,  -148,   143,  -148,   145,  -148,    86,
     102,   133,  -148,   146,    86,  -148,   147,   118,  -148,   148,
     118,  -148,    84,     3,  -148,   119,  -148,  -148,  -148,  -148,
    -148,  -148,  -148,  -148,  -148,  -148,   149,  -148,  -148,   151,
     152,  -148,    34,   153,   155,  -148,   156,  -148,   118,   157,
    -148,  -148,  -148,  -148,  -148,  -148,   158,   159,    80,  -148,
    -148
};

  /* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
     Performed when YYTABLE does not specify something else to do.  Zero
     means the default is an error.  */
static const yytype_uint8 yydefact[] =
{
       0,     0,     0,     0,     0,     1,     0,     2,     0,     0,
       0,     0,    15,     0,     0,    47,    19,     0,    16,     0,
       0,     0,    18,     0,    48,    30,    68,     4,     4,     4,
      70,    17,     4,    10,     0,     0,     0,    52,     0,     0,
       0,    20,     4,    22,    60,    28,    24,     0,    26,    70,
      72,    55,    56,     0,    87,    57,    54,    10,     0,    50,
      49,     0,     0,     0,     3,     0,     0,     0,     0,    65,
       0,    69,     0,     0,     9,     8,    75,     0,     0,     0,
       0,     0,     0,     0,    14,    58,    53,     0,     0,     0,
       0,    31,    34,    65,    21,    23,     4,    29,    25,    65,
      73,    27,    71,     0,    95,     0,    77,    81,     0,    85,
       0,    41,    12,    11,     0,    41,    14,     6,    51,    41,
       0,   108,    38,    37,     0,    33,    66,    59,     0,    64,
       0,     0,    99,    98,     0,    76,    94,     0,     0,    79,
       0,    83,    43,     0,    39,    40,    45,    88,     0,    13,
       0,    35,   100,     0,     0,     0,     0,    32,     0,     0,
      65,    74,     0,    78,    82,     0,    86,     0,    42,     0,
       0,     0,    44,     0,     0,     7,     0,   102,    97,     0,
       0,   109,    41,     0,    67,     0,    61,     5,    80,    84,
      90,    46,    89,    92,    36,    96,     0,   101,   104,     0,
       0,   111,     4,     0,     0,   103,     0,   110,     0,     0,
      62,    91,    93,   105,   106,   112,     0,     0,    65,   107,
      63
};

  /* YYPGOTO[NTERM-NUM].  */
static const yytype_int16 yypgoto[] =
{
    -148,  -148,   -27,  -148,   -51,  -148,    83,   -71,    47,  -148,
    -148,  -148,  -148,  -148,  -148,  -148,  -148,  -148,  -148,  -148,
    -148,    72,  -148,  -148,  -111,  -114,    20,  -148,  -148,  -148,
    -148,  -148,  -148,  -148,   127,   -19,   -90,  -148,  -148,  -148,
     123,  -148,  -148,  -148,   -32,  -148,  -148,  -148,  -148,  -148,
    -148,  -148,  -148,  -148,    90,  -148,  -128,  -147,  -148,  -148,
    -148,  -148,  -148,  -148
};

  /* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,     2,    44,    74,    54,   150,    52,   116,   117,     3,
      14,    21,    18,    22,    63,    65,    68,    70,    67,    40,
     124,    91,    92,   176,   143,   144,   145,   146,     7,    17,
      39,    24,    87,    58,    37,    45,   100,   216,    41,   158,
      48,    49,    50,   130,   104,   105,   137,   165,   138,   167,
     140,   173,   203,   204,   106,   177,   134,   122,   179,   196,
     206,   217,   199,   209
};

  /* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
     positive, shift that token.  If negative, reduce the rule whose
     number is the opposite.  If YYTABLE_NINF, syntax error.  */
static const yytype_uint8 yytable[] =
{
      43,    75,    55,   126,   148,   178,   162,   131,   151,   129,
      46,   114,   159,    51,    25,    64,   112,   113,    26,    27,
      28,   128,     1,    32,    33,    34,    35,   103,   168,     4,
     195,     5,   171,   198,    75,    84,    29,     6,   121,   132,
       8,    36,   160,   147,    30,     9,   108,   109,   110,    10,
      11,    99,   133,    12,   152,   201,   153,   154,   123,   155,
      15,   214,    88,    89,    90,    13,   156,    16,    42,    84,
     186,   200,    19,    31,   136,   136,   139,   127,   141,    20,
      23,    38,    47,   133,    76,    77,    78,    79,    80,    81,
      53,    82,    83,    56,    57,    60,    61,    62,    73,    84,
      66,   121,    69,   181,    72,   183,    86,    93,    94,    95,
      97,    98,   101,    96,   111,    99,   102,   115,   118,   142,
     119,   180,   120,   135,   157,   161,   121,   163,   220,   121,
      84,   164,   133,   166,   169,   174,   191,   190,   170,   175,
      85,   131,   193,   182,   184,   187,   188,   185,   189,   192,
     194,   197,   205,   202,   207,   208,   211,   121,   212,   213,
     215,   218,   219,   149,   125,    59,   172,   107,     0,     0,
       0,     0,    71,     0,     0,   210
};

static const yytype_int16 yycheck[] =
{
      27,    52,    34,    93,   115,   152,   134,     4,   119,    99,
      29,    82,     4,    32,     7,    42,    34,    35,    11,    12,
      13,     5,     4,    14,    15,    16,    17,    26,   142,     6,
     177,     0,   146,   180,    85,    34,    29,     4,    89,    36,
       4,    32,    34,   114,    37,     6,    78,    79,    80,    17,
       4,    35,   103,    34,    19,   183,    21,    22,    90,    24,
      34,   208,     8,     9,    10,    25,    31,     3,    34,    34,
     160,   182,     3,     3,   106,   107,   108,    96,   110,     4,
       4,     4,     4,   134,    19,    20,    21,    22,    23,    24,
       4,    26,    27,    34,    33,     3,    34,     4,     4,    34,
       5,   152,    34,   154,     5,   156,     3,    34,     3,     3,
       3,     3,     3,    34,     4,    35,    34,     4,     3,    35,
       4,   153,     4,     3,     3,     3,   177,     3,   218,   180,
      34,     3,   183,     3,     3,     3,    34,   169,     5,     3,
      57,     4,   174,     4,     3,     3,     3,    30,     3,     3,
       3,     3,     3,    34,     3,     3,     3,   208,     3,     3,
       3,     3,     3,   116,    92,    38,   146,    77,    -1,    -1,
      -1,    -1,    49,    -1,    -1,   202
};

  /* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
     symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,     4,    39,    47,     6,     0,     4,    66,     4,     6,
      17,     4,    34,    25,    48,    34,     3,    67,    50,     3,
       4,    49,    51,     4,    69,     7,    11,    12,    13,    29,
      37,     3,    14,    15,    16,    17,    32,    72,     4,    68,
      57,    76,    34,    40,    40,    73,    73,     4,    78,    79,
      80,    73,    44,     4,    42,    82,    34,    33,    71,    72,
       3,    34,     4,    52,    40,    53,     5,    56,    54,    34,
      55,    78,     5,     4,    41,    42,    19,    20,    21,    22,
      23,    24,    26,    27,    34,    44,     3,    70,     8,     9,
      10,    59,    60,    34,     3,     3,    34,     3,     3,    35,
      74,     3,    34,    26,    82,    83,    92,    92,    82,    82,
      82,     4,    34,    35,    45,     4,    45,    46,     3,     4,
       4,    42,    95,    82,    58,    59,    74,    73,     5,    74,
      81,     4,    36,    42,    94,     3,    82,    84,    86,    82,
      88,    82,    35,    62,    63,    64,    65,    45,    62,    46,
      43,    62,    19,    21,    22,    24,    31,     3,    77,     4,
      34,     3,    94,     3,     3,    85,     3,    87,    63,     3,
       5,    63,    64,    89,     3,     3,    61,    93,    95,    96,
      82,    42,     4,    42,     3,    30,    74,     3,     3,     3,
      82,    34,     3,    82,     3,    95,    97,     3,    95,   100,
      62,    94,    34,    90,    91,     3,    98,     3,     3,   101,
      40,     3,     3,     3,    95,     3,    75,    99,     3,     3,
      74
};

  /* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,    38,    39,    40,    40,    41,    43,    42,    44,    44,
      44,    45,    45,    46,    46,    48,    49,    47,    50,    50,
      52,    51,    53,    51,    54,    51,    55,    51,    56,    51,
      57,    58,    51,    59,    59,    61,    60,    60,    60,    62,
      62,    62,    63,    63,    64,    64,    65,    67,    68,    66,
      70,    69,    71,    69,    72,    72,    72,    72,    72,    73,
      73,    74,    75,    74,    74,    74,    77,    76,    76,    78,
      78,    79,    79,    81,    80,    83,    82,    84,    82,    85,
      82,    86,    82,    87,    82,    88,    82,    82,    89,    82,
      90,    82,    91,    82,    92,    92,    93,    93,    94,    94,
      96,    95,    97,    95,    98,    95,    99,    95,    95,   100,
      95,   101,    95
};

  /* YYR2[YYN] -- Number of symbols on the right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     2,     2,     0,     5,     0,     5,     2,     2,
       0,     1,     1,     2,     0,     0,     0,    10,     2,     0,
       0,     5,     0,     5,     0,     5,     0,     5,     0,     5,
       0,     0,     7,     2,     1,     0,     5,     2,     2,     1,
       1,     0,     2,     1,     2,     1,     3,     0,     0,    10,
       0,     5,     0,     4,     2,     2,     2,     2,     3,     4,
       1,     4,     0,     9,     2,     0,     0,     6,     0,     2,
       0,     3,     1,     0,     5,     0,     4,     0,     5,     0,
       6,     0,     5,     0,     6,     0,     5,     1,     0,     6,
       0,     8,     0,     8,     2,     1,     2,     1,     1,     1,
       0,     4,     0,     5,     0,     6,     0,     8,     1,     0,
       5,     0,     6
};


#define yyerrok         (yyerrstatus = 0)
#define yyclearin       (yychar = YYEMPTY)
#define YYEMPTY         (-2)
#define YYEOF           0

#define YYACCEPT        goto yyacceptlab
#define YYABORT         goto yyabortlab
#define YYERROR         goto yyerrorlab


#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)                                  \
do                                                              \
  if (yychar == YYEMPTY)                                        \
    {                                                           \
      yychar = (Token);                                         \
      yylval = (Value);                                         \
      YYPOPSTACK (yylen);                                       \
      yystate = *yyssp;                                         \
      goto yybackup;                                            \
    }                                                           \
  else                                                          \
    {                                                           \
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;                                                  \
    }                                                           \
while (0)

/* Error token number */
#define YYTERROR        1
#define YYERRCODE       256



/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)                        \
do {                                            \
  if (yydebug)                                  \
    YYFPRINTF Args;                             \
} while (0)

/* This macro is provided for backward compatibility. */
#ifndef YY_LOCATION_PRINT
# define YY_LOCATION_PRINT(File, Loc) ((void) 0)
#endif


# define YY_SYMBOL_PRINT(Title, Type, Value, Location)                    \
do {                                                                      \
  if (yydebug)                                                            \
    {                                                                     \
      YYFPRINTF (stderr, "%s ", Title);                                   \
      yy_symbol_print (stderr,                                            \
                  Type, Value); \
      YYFPRINTF (stderr, "\n");                                           \
    }                                                                     \
} while (0)


/*----------------------------------------.
| Print this symbol's value on YYOUTPUT.  |
`----------------------------------------*/

static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
{
  FILE *yyo = yyoutput;
  YYUSE (yyo);
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# endif
  YYUSE (yytype);
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
{
  YYFPRINTF (yyoutput, "%s %s (",
             yytype < YYNTOKENS ? "token" : "nterm", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)                            \
do {                                                            \
  if (yydebug)                                                  \
    yy_stack_print ((Bottom), (Top));                           \
} while (0)


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

static void
yy_reduce_print (yytype_int16 *yyssp, YYSTYPE *yyvsp, int yyrule)
{
  unsigned long int yylno = yyrline[yyrule];
  int yynrhs = yyr2[yyrule];
  int yyi;
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
             yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr,
                       yystos[yyssp[yyi + 1 - yynrhs]],
                       &(yyvsp[(yyi + 1) - (yynrhs)])
                                              );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)          \
do {                                    \
  if (yydebug)                          \
    yy_reduce_print (yyssp, yyvsp, Rule); \
} while (0)

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif


#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
static YYSIZE_T
yystrlen (const char *yystr)
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
static char *
yystpcpy (char *yydest, const char *yysrc)
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
        switch (*++yyp)
          {
          case '\'':
          case ',':
            goto do_not_strip_quotes;

          case '\\':
            if (*++yyp != '\\')
              goto do_not_strip_quotes;
            /* Fall through.  */
          default:
            if (yyres)
              yyres[yyn] = *yyp;
            yyn++;
            break;

          case '"':
            if (yyres)
              yyres[yyn] = '\0';
            return yyn;
          }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into *YYMSG, which is of size *YYMSG_ALLOC, an error message
   about the unexpected token YYTOKEN for the state stack whose top is
   YYSSP.

   Return 0 if *YYMSG was successfully written.  Return 1 if *YYMSG is
   not large enough to hold the message.  In that case, also set
   *YYMSG_ALLOC to the required number of bytes.  Return 2 if the
   required number of bytes is too large to store.  */
static int
yysyntax_error (YYSIZE_T *yymsg_alloc, char **yymsg,
                yytype_int16 *yyssp, int yytoken)
{
  YYSIZE_T yysize0 = yytnamerr (YY_NULLPTR, yytname[yytoken]);
  YYSIZE_T yysize = yysize0;
  enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
  /* Internationalized format string. */
  const char *yyformat = YY_NULLPTR;
  /* Arguments of yyformat. */
  char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
  /* Number of reported tokens (one for the "unexpected", one per
     "expected"). */
  int yycount = 0;

  /* There are many possibilities here to consider:
     - If this state is a consistent state with a default action, then
       the only way this function was invoked is if the default action
       is an error action.  In that case, don't check for expected
       tokens because there are none.
     - The only way there can be no lookahead present (in yychar) is if
       this state is a consistent state with a default action.  Thus,
       detecting the absence of a lookahead is sufficient to determine
       that there is no unexpected or expected token to report.  In that
       case, just report a simple "syntax error".
     - Don't assume there isn't a lookahead just because this state is a
       consistent state with a default action.  There might have been a
       previous inconsistent state, consistent state with a non-default
       action, or user semantic action that manipulated yychar.
     - Of course, the expected token list depends on states to have
       correct lookahead information, and it depends on the parser not
       to perform extra reductions after fetching a lookahead from the
       scanner and before detecting a syntax error.  Thus, state merging
       (from LALR or IELR) and default reductions corrupt the expected
       token list.  However, the list is correct for canonical LR with
       one exception: it will still contain any token that will not be
       accepted due to an error action in a later state.
  */
  if (yytoken != YYEMPTY)
    {
      int yyn = yypact[*yyssp];
      yyarg[yycount++] = yytname[yytoken];
      if (!yypact_value_is_default (yyn))
        {
          /* Start YYX at -YYN if negative to avoid negative indexes in
             YYCHECK.  In other words, skip the first -YYN actions for
             this state because they are default actions.  */
          int yyxbegin = yyn < 0 ? -yyn : 0;
          /* Stay within bounds of both yycheck and yytname.  */
          int yychecklim = YYLAST - yyn + 1;
          int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
          int yyx;

          for (yyx = yyxbegin; yyx < yyxend; ++yyx)
            if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR
                && !yytable_value_is_error (yytable[yyx + yyn]))
              {
                if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
                  {
                    yycount = 1;
                    yysize = yysize0;
                    break;
                  }
                yyarg[yycount++] = yytname[yyx];
                {
                  YYSIZE_T yysize1 = yysize + yytnamerr (YY_NULLPTR, yytname[yyx]);
                  if (! (yysize <= yysize1
                         && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
                    return 2;
                  yysize = yysize1;
                }
              }
        }
    }

  switch (yycount)
    {
# define YYCASE_(N, S)                      \
      case N:                               \
        yyformat = S;                       \
      break
      YYCASE_(0, YY_("syntax error"));
      YYCASE_(1, YY_("syntax error, unexpected %s"));
      YYCASE_(2, YY_("syntax error, unexpected %s, expecting %s"));
      YYCASE_(3, YY_("syntax error, unexpected %s, expecting %s or %s"));
      YYCASE_(4, YY_("syntax error, unexpected %s, expecting %s or %s or %s"));
      YYCASE_(5, YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s"));
# undef YYCASE_
    }

  {
    YYSIZE_T yysize1 = yysize + yystrlen (yyformat);
    if (! (yysize <= yysize1 && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
      return 2;
    yysize = yysize1;
  }

  if (*yymsg_alloc < yysize)
    {
      *yymsg_alloc = 2 * yysize;
      if (! (yysize <= *yymsg_alloc
             && *yymsg_alloc <= YYSTACK_ALLOC_MAXIMUM))
        *yymsg_alloc = YYSTACK_ALLOC_MAXIMUM;
      return 1;
    }

  /* Avoid sprintf, as that infringes on the user's name space.
     Don't have undefined behavior even if the translation
     produced a string with the wrong number of "%s"s.  */
  {
    char *yyp = *yymsg;
    int yyi = 0;
    while ((*yyp = *yyformat) != '\0')
      if (*yyp == '%' && yyformat[1] == 's' && yyi < yycount)
        {
          yyp += yytnamerr (yyp, yyarg[yyi++]);
          yyformat += 2;
        }
      else
        {
          yyp++;
          yyformat++;
        }
  }
  return 0;
}
#endif /* YYERROR_VERBOSE */

/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
{
  YYUSE (yyvaluep);
  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  YYUSE (yytype);
  YY_IGNORE_MAYBE_UNINITIALIZED_END
}




/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;
/* Number of syntax errors so far.  */
int yynerrs;


/*----------.
| yyparse.  |
`----------*/

int
yyparse (void)
{
    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       'yyss': related to states.
       'yyvs': related to semantic values.

       Refer to the stacks through separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken = 0;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yyssp = yyss = yyssa;
  yyvsp = yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */
  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
        /* Give user a chance to reallocate the stack.  Use copies of
           these so that the &'s don't force the real ones into
           memory.  */
        YYSTYPE *yyvs1 = yyvs;
        yytype_int16 *yyss1 = yyss;

        /* Each stack pointer address is followed by the size of the
           data in use in that stack, in bytes.  This used to be a
           conditional around just the two extra args, but that might
           be undefined if yyoverflow is a macro.  */
        yyoverflow (YY_("memory exhausted"),
                    &yyss1, yysize * sizeof (*yyssp),
                    &yyvs1, yysize * sizeof (*yyvsp),
                    &yystacksize);

        yyss = yyss1;
        yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
        goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
        yystacksize = YYMAXDEPTH;

      {
        yytype_int16 *yyss1 = yyss;
        union yyalloc *yyptr =
          (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
        if (! yyptr)
          goto yyexhaustedlab;
        YYSTACK_RELOCATE (yyss_alloc, yyss);
        YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
        if (yyss1 != yyssa)
          YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
                  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
        YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yypact_value_is_default (yyn))
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = yylex ();
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yytable_value_is_error (yyn))
        goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     '$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 3:
#line 58 "parser.y" /* yacc.c:1646  */
    { (yyval.intlistp) = intcons((yyvsp[-1].i),(yyvsp[0].intlistp)); }
#line 1415 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 4:
#line 59 "parser.y" /* yacc.c:1646  */
    { (yyval.intlistp) = EMPTYLIST; }
#line 1421 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 5:
#line 62 "parser.y" /* yacc.c:1646  */
    { }
#line 1427 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 6:
#line 65 "parser.y" /* yacc.c:1646  */
    { rparen("term"); }
#line 1433 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 7:
#line 65 "parser.y" /* yacc.c:1646  */
    { (yyval.atomp) = newatom((yyvsp[-3].i),(yyvsp[-2].intlistp)); }
#line 1439 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 8:
#line 68 "parser.y" /* yacc.c:1646  */
    { (yyval.atomlistp) = atomcons((yyvsp[0].atomp),(yyvsp[-1].atomlistp)); }
#line 1445 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 9:
#line 69 "parser.y" /* yacc.c:1646  */
    { (yyval.atomlistp) = (yyvsp[-1].atomlistp); }
#line 1451 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 10:
#line 70 "parser.y" /* yacc.c:1646  */
    { (yyval.atomlistp) = EMPTYLIST; }
#line 1457 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 11:
#line 73 "parser.y" /* yacc.c:1646  */
    { (yyval.i) = (yyvsp[0].i); }
#line 1463 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 12:
#line 74 "parser.y" /* yacc.c:1646  */
    { (yyval.i) = (yyvsp[0].i); }
#line 1469 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 13:
#line 77 "parser.y" /* yacc.c:1646  */
    { (yyval.intlistp) = intcons((yyvsp[-1].i),(yyvsp[0].intlistp)); }
#line 1475 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 14:
#line 78 "parser.y" /* yacc.c:1646  */
    { (yyval.intlistp) = EMPTYLIST; }
#line 1481 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 15:
#line 83 "parser.y" /* yacc.c:1646  */
    { rparen("domain"); }
#line 1487 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 16:
#line 83 "parser.y" /* yacc.c:1646  */
    { rparen("domain body"); }
#line 1493 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 17:
#line 83 "parser.y" /* yacc.c:1646  */
    { storedomain((yyvsp[-5].i)); }
#line 1499 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 19:
#line 87 "parser.y" /* yacc.c:1646  */
    { }
#line 1505 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 20:
#line 90 "parser.y" /* yacc.c:1646  */
    { rparen(":predicates"); }
#line 1511 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 21:
#line 90 "parser.y" /* yacc.c:1646  */
    { storepredicates(); }
#line 1517 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 22:
#line 91 "parser.y" /* yacc.c:1646  */
    { rparen(":requirements"); }
#line 1523 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 23:
#line 91 "parser.y" /* yacc.c:1646  */
    { checkrequirements((yyvsp[-2].intlistp)); }
#line 1529 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 24:
#line 92 "parser.y" /* yacc.c:1646  */
    { rparen(":constants"); }
#line 1535 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 25:
#line 92 "parser.y" /* yacc.c:1646  */
    { storeconstants((yyvsp[-2].typedvarlistp)); }
#line 1541 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 26:
#line 93 "parser.y" /* yacc.c:1646  */
    { rparen(":functions"); }
#line 1547 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 27:
#line 93 "parser.y" /* yacc.c:1646  */
    { }
#line 1553 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 28:
#line 94 "parser.y" /* yacc.c:1646  */
    { rparen(":types"); }
#line 1559 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 29:
#line 94 "parser.y" /* yacc.c:1646  */
    { storetypes((yyvsp[-2].typedvarlistp)); }
#line 1565 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 30:
#line 95 "parser.y" /* yacc.c:1646  */
    { COST = 0; }
#line 1571 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 31:
#line 95 "parser.y" /* yacc.c:1646  */
    { rparen(":action"); }
#line 1577 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 32:
#line 95 "parser.y" /* yacc.c:1646  */
    { addactioncost(COST); addnewaction((yyvsp[-3].i)); }
#line 1583 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 35:
#line 102 "parser.y" /* yacc.c:1646  */
    { rparen(":action definitions"); }
#line 1589 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 36:
#line 102 "parser.y" /* yacc.c:1646  */
    { addactionparameters((yyvsp[-2].typedvarlistp)); }
#line 1595 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 37:
#line 103 "parser.y" /* yacc.c:1646  */
    { addactionprecond((yyvsp[0].Sfmap)); }
#line 1601 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 38:
#line 104 "parser.y" /* yacc.c:1646  */
    { addactioneffect((yyvsp[0].Seffp)); }
#line 1607 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 39:
#line 107 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = withtype(UNIVTYPE,(yyvsp[0].intlistp)); }
#line 1613 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 40:
#line 108 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = (yyvsp[0].typedvarlistp); }
#line 1619 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 41:
#line 109 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = EMPTYLIST; }
#line 1625 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 42:
#line 112 "parser.y" /* yacc.c:1646  */
    { (yyval.intlistp) = intcons((yyvsp[-1].i),(yyvsp[0].intlistp)); }
#line 1631 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 43:
#line 113 "parser.y" /* yacc.c:1646  */
    { (yyval.intlistp) = intcons((yyvsp[0].i), EMPTYLIST); }
#line 1637 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 44:
#line 116 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = TVappend((yyvsp[-1].typedvarlistp),(yyvsp[0].typedvarlistp)); }
#line 1643 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 45:
#line 117 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = (yyvsp[0].typedvarlistp); }
#line 1649 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 46:
#line 120 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = withtype((yyvsp[0].i),(yyvsp[-2].intlistp)); }
#line 1655 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 47:
#line 124 "parser.y" /* yacc.c:1646  */
    { rparen(":problem"); }
#line 1661 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 48:
#line 124 "parser.y" /* yacc.c:1646  */
    { rparen("problem definition"); }
#line 1667 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 49:
#line 124 "parser.y" /* yacc.c:1646  */
    { addproblem((yyvsp[-5].i)); }
#line 1673 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 50:
#line 127 "parser.y" /* yacc.c:1646  */
    { rparen("problem definitions"); }
#line 1679 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 51:
#line 127 "parser.y" /* yacc.c:1646  */
    { }
#line 1685 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 52:
#line 128 "parser.y" /* yacc.c:1646  */
    { rparen("problem definitions"); }
#line 1691 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 53:
#line 128 "parser.y" /* yacc.c:1646  */
    { }
#line 1697 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 54:
#line 131 "parser.y" /* yacc.c:1646  */
    { checkdomain((yyvsp[0].i)); }
#line 1703 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 55:
#line 132 "parser.y" /* yacc.c:1646  */
    { storeobjects((yyvsp[0].typedvarlistp)); }
#line 1709 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 56:
#line 133 "parser.y" /* yacc.c:1646  */
    { storeinit((yyvsp[0].atomlistp)); }
#line 1715 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 57:
#line 134 "parser.y" /* yacc.c:1646  */
    { storegoal((yyvsp[0].Sfmap)); }
#line 1721 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 58:
#line 135 "parser.y" /* yacc.c:1646  */
    { }
#line 1727 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 59:
#line 139 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = TVappend(withtype((yyvsp[-1].i),(yyvsp[-3].intlistp)),(yyvsp[0].typedvarlistp)); }
#line 1733 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 60:
#line 140 "parser.y" /* yacc.c:1646  */
    { (yyval.typedvarlistp) = withtype(UNIVTYPE,(yyvsp[0].intlistp)); }
#line 1739 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 61:
#line 143 "parser.y" /* yacc.c:1646  */
    { }
#line 1745 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 62:
#line 144 "parser.y" /* yacc.c:1646  */
    { rparen("typed variable list"); }
#line 1751 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 63:
#line 144 "parser.y" /* yacc.c:1646  */
    { }
#line 1757 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 64:
#line 145 "parser.y" /* yacc.c:1646  */
    { }
#line 1763 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 65:
#line 146 "parser.y" /* yacc.c:1646  */
    { }
#line 1769 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 66:
#line 149 "parser.y" /* yacc.c:1646  */
    { rparen("typed atom list"); }
#line 1775 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 67:
#line 149 "parser.y" /* yacc.c:1646  */
    { }
#line 1781 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 68:
#line 150 "parser.y" /* yacc.c:1646  */
    { }
#line 1787 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 69:
#line 153 "parser.y" /* yacc.c:1646  */
    { }
#line 1793 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 70:
#line 154 "parser.y" /* yacc.c:1646  */
    { }
#line 1799 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 71:
#line 157 "parser.y" /* yacc.c:1646  */
    { }
#line 1805 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 72:
#line 158 "parser.y" /* yacc.c:1646  */
    { }
#line 1811 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 73:
#line 161 "parser.y" /* yacc.c:1646  */
    { rparen("function list"); }
#line 1817 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 75:
#line 166 "parser.y" /* yacc.c:1646  */
    { rparen("empty conjunction"); }
#line 1823 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 76:
#line 166 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Strue(); }
#line 1829 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 77:
#line 167 "parser.y" /* yacc.c:1646  */
    { rparen("conjunction"); }
#line 1835 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 78:
#line 167 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sconjunction((yyvsp[-2].Sfmalistp)); }
#line 1841 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 79:
#line 168 "parser.y" /* yacc.c:1646  */
    { rparen("when"); }
#line 1847 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 80:
#line 168 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sconjunction(Sfmacons(Sneg((yyvsp[-3].Sfmap)),Sfmacons((yyvsp[-2].Sfmap),EMPTYLIST))); }
#line 1853 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 81:
#line 169 "parser.y" /* yacc.c:1646  */
    { rparen("disjunction"); }
#line 1859 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 82:
#line 169 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sdisjunction((yyvsp[-2].Sfmalistp)); }
#line 1865 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 83:
#line 170 "parser.y" /* yacc.c:1646  */
    { rparen("imply"); }
#line 1871 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 84:
#line 170 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sdisjunction(Sfmacons(Sneg((yyvsp[-3].Sfmap)),Sfmacons((yyvsp[-2].Sfmap),EMPTYLIST))); }
#line 1877 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 85:
#line 171 "parser.y" /* yacc.c:1646  */
    { rparen("not"); }
#line 1883 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 86:
#line 171 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sneg((yyvsp[-2].Sfmap)); }
#line 1889 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 87:
#line 172 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Satom((yyvsp[0].atomp)); }
#line 1895 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 88:
#line 173 "parser.y" /* yacc.c:1646  */
    { rparen("equality"); }
#line 1901 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 89:
#line 173 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = SfmaEQ((yyvsp[-3].i),(yyvsp[-2].i)); }
#line 1907 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 90:
#line 174 "parser.y" /* yacc.c:1646  */
    { rparen("forall"); }
#line 1913 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 91:
#line 174 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sfmaforall((yyvsp[-4].typedvarlistp),(yyvsp[-2].Sfmap)); }
#line 1919 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 92:
#line 175 "parser.y" /* yacc.c:1646  */
    { rparen("exists"); }
#line 1925 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 93:
#line 175 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmap) = Sfmaforsome((yyvsp[-4].typedvarlistp),(yyvsp[-2].Sfmap)); }
#line 1931 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 94:
#line 178 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmalistp) = Sfmacons((yyvsp[0].Sfmap),(yyvsp[-1].Sfmalistp)); }
#line 1937 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 95:
#line 179 "parser.y" /* yacc.c:1646  */
    { (yyval.Sfmalistp) = Sfmacons((yyvsp[0].Sfmap),EMPTYLIST); }
#line 1943 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 96:
#line 182 "parser.y" /* yacc.c:1646  */
    { (yyval.Sefflistp) = Seffcons((yyvsp[0].Seffp),(yyvsp[-1].Sefflistp)); }
#line 1949 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 97:
#line 183 "parser.y" /* yacc.c:1646  */
    { (yyval.Sefflistp) = Seffcons((yyvsp[0].Seffp),EMPTYLIST); }
#line 1955 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 98:
#line 186 "parser.y" /* yacc.c:1646  */
    { (yyval.i) = 0; }
#line 1961 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 99:
#line 187 "parser.y" /* yacc.c:1646  */
    { (yyval.i) = (yyvsp[0].i); }
#line 1967 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 100:
#line 190 "parser.y" /* yacc.c:1646  */
    { rparen("empty conjunction"); }
#line 1973 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 101:
#line 190 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = Seffconjunction(EMPTYLIST); }
#line 1979 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 102:
#line 191 "parser.y" /* yacc.c:1646  */
    { rparen("compound effect"); }
#line 1985 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 103:
#line 191 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = Seffconjunction((yyvsp[-2].Sefflistp)); }
#line 1991 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 104:
#line 192 "parser.y" /* yacc.c:1646  */
    { rparen("when"); }
#line 1997 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 105:
#line 192 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = Seffwhen((yyvsp[-3].Sfmap),(yyvsp[-2].Seffp)); }
#line 2003 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 106:
#line 193 "parser.y" /* yacc.c:1646  */
    { rparen("forall"); }
#line 2009 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 107:
#line 193 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = Seffforall((yyvsp[-4].typedvarlistp),(yyvsp[-2].Seffp)); }
#line 2015 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 108:
#line 194 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = SPeffatom((yyvsp[0].atomp)); }
#line 2021 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 109:
#line 195 "parser.y" /* yacc.c:1646  */
    { rparen("not"); }
#line 2027 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 110:
#line 195 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = SNeffatom((yyvsp[-2].atomp)); }
#line 2033 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 111:
#line 196 "parser.y" /* yacc.c:1646  */
    { rparen("increase"); }
#line 2039 "parser.tab.c" /* yacc.c:1646  */
    break;

  case 112:
#line 196 "parser.y" /* yacc.c:1646  */
    { (yyval.Seffp) = Seffconjunction(NULL); COST = (yyvsp[-2].i); }
#line 2045 "parser.tab.c" /* yacc.c:1646  */
    break;


#line 2049 "parser.tab.c" /* yacc.c:1646  */
      default: break;
    }
  /* User semantic actions sometimes alter yychar, and that requires
     that yytoken be updated with the new translation.  We take the
     approach of translating immediately before every use of yytoken.
     One alternative is translating here after every semantic action,
     but that translation would be missed if the semantic action invokes
     YYABORT, YYACCEPT, or YYERROR immediately after altering yychar or
     if it invokes YYBACKUP.  In the case of YYABORT or YYACCEPT, an
     incorrect destructor might then be invoked immediately.  In the
     case of YYERROR or YYBACKUP, subsequent parser actions might lead
     to an incorrect destructor call or verbose syntax error message
     before the lookahead is translated.  */
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now 'shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*--------------------------------------.
| yyerrlab -- here on detecting error.  |
`--------------------------------------*/
yyerrlab:
  /* Make sure we have latest lookahead translation.  See comments at
     user semantic actions for why this is necessary.  */
  yytoken = yychar == YYEMPTY ? YYEMPTY : YYTRANSLATE (yychar);

  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
# define YYSYNTAX_ERROR yysyntax_error (&yymsg_alloc, &yymsg, \
                                        yyssp, yytoken)
      {
        char const *yymsgp = YY_("syntax error");
        int yysyntax_error_status;
        yysyntax_error_status = YYSYNTAX_ERROR;
        if (yysyntax_error_status == 0)
          yymsgp = yymsg;
        else if (yysyntax_error_status == 1)
          {
            if (yymsg != yymsgbuf)
              YYSTACK_FREE (yymsg);
            yymsg = (char *) YYSTACK_ALLOC (yymsg_alloc);
            if (!yymsg)
              {
                yymsg = yymsgbuf;
                yymsg_alloc = sizeof yymsgbuf;
                yysyntax_error_status = 2;
              }
            else
              {
                yysyntax_error_status = YYSYNTAX_ERROR;
                yymsgp = yymsg;
              }
          }
        yyerror (yymsgp);
        if (yysyntax_error_status == 2)
          goto yyexhaustedlab;
      }
# undef YYSYNTAX_ERROR
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
         error, discard it.  */

      if (yychar <= YYEOF)
        {
          /* Return failure if at end of input.  */
          if (yychar == YYEOF)
            YYABORT;
        }
      else
        {
          yydestruct ("Error: discarding",
                      yytoken, &yylval);
          yychar = YYEMPTY;
        }
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule whose action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;      /* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (!yypact_value_is_default (yyn))
        {
          yyn += YYTERROR;
          if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
            {
              yyn = yytable[yyn];
              if (0 < yyn)
                break;
            }
        }

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
        YYABORT;


      yydestruct ("Error: popping",
                  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined yyoverflow || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
    {
      /* Make sure we have latest lookahead translation.  See comments at
         user semantic actions for why this is necessary.  */
      yytoken = YYTRANSLATE (yychar);
      yydestruct ("Cleanup: discarding lookahead",
                  yytoken, &yylval);
    }
  /* Do not reclaim the symbols of the rule whose action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
                  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  return yyresult;
}
#line 199 "parser.y" /* yacc.c:1906  */


void parseerror(char *s) {
  errorstring = s;
}

void rparen(char *s) {
  errorstring = s;
}
