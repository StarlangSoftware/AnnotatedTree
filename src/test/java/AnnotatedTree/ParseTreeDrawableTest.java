package AnnotatedTree;

import ParseTree.ParseTree;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class ParseTreeDrawableTest {
    ParseTreeDrawable tree0, tree1, tree2, tree3, tree4;
    ParseTreeDrawable tree5, tree6, tree7, tree8, tree9;

    @Before
    public void setUp() throws Exception {
        tree0 = new ParseTreeDrawable(new FileInputStream("trees/0000.dev"));
        tree1 = new ParseTreeDrawable(new FileInputStream("trees/0001.dev"));
        tree2 = new ParseTreeDrawable(new FileInputStream("trees/0002.dev"));
        tree3 = new ParseTreeDrawable(new FileInputStream("trees/0003.dev"));
        tree4 = new ParseTreeDrawable(new FileInputStream("trees/0004.dev"));
        tree5 = new ParseTreeDrawable(new FileInputStream("trees/0005.dev"));
        tree6 = new ParseTreeDrawable(new FileInputStream("trees/0006.dev"));
        tree7 = new ParseTreeDrawable(new FileInputStream("trees/0007.dev"));
        tree8 = new ParseTreeDrawable(new FileInputStream("trees/0008.dev"));
        tree9 = new ParseTreeDrawable(new FileInputStream("trees/0009.dev"));
    }

    @Test
    public void testGenerateParseTree() {
        ParseTree tree;
        tree = tree0.generateParseTree(true);
        assertEquals("(S (NP (NP (ADJP (ADJP yeni) (ADJP Büyük))  (NP yasada))  (NP (ADJP karmaşık) (NP dil)) )  (VP (NP savaşı) (VP bulandırmıştır))  (. .)) ", tree.toString());
        tree = tree0.generateParseTree(false);
        assertEquals("(S (NP (NP (ADJP (ADJP yeni) (ADJP büyük))  (NP yasa))  (NP (ADJP karmaşık) (NP dil)) )  (VP (NP savaş) (VP bulan))  (. .)) ", tree.toString());
        tree = tree1.generateParseTree(true);
        assertEquals("(S (NP (NP (NP Yollar) (CONJP ve) (NP Araçlar))  (NP Komitesi))  (VP (ADVP (ADJP gelecek) (NP Salı))  (NP (PP (NP fatura) (PP için))  (NP (DP bir) (NP duruşma)) )  (VP yapacak))  (. .)) ", tree.toString());
        tree = tree1.generateParseTree(false);
        assertEquals("(S (NP (NP (NP yol) (CONJP ve) (NP araç))  (NP komite))  (VP (ADVP (ADJP gelecek) (NP salı))  (NP (PP (NP fatura) (PP için))  (NP (DP bir) (NP duruşma)) )  (VP yap))  (. .)) ", tree.toString());
        tree = tree2.generateParseTree(true);
        assertEquals("(S (NP Biz) (VP (S (NP reklamın) (NP (NP işe) (ADVP yarayıp) (NP yaramadığını)) )  (NP görmek) (VP üzereyiz))  (. .)) ", tree.toString());
        tree = tree2.generateParseTree(false);
        assertEquals("(S (NP biz) (VP (S (NP reklam) (NP (NP iş) (ADVP yara) (NP yara)) )  (NP gör) (VP üzere))  (. .)) ", tree.toString());
    }

    @Test
    public void testMaxDepth() {
        assertEquals(5, tree0.maxDepth());
        assertEquals(5, tree1.maxDepth());
        assertEquals(5, tree2.maxDepth());
        assertEquals(5, tree3.maxDepth());
        assertEquals(6, tree4.maxDepth());
        assertEquals(3, tree5.maxDepth());
        assertEquals(4, tree6.maxDepth());
        assertEquals(6, tree7.maxDepth());
        assertEquals(5, tree8.maxDepth());
        assertEquals(6, tree9.maxDepth());
    }

    @Test
    public void testGenerateAnnotatedSentence() {
        assertEquals("{turkish=yeni}{morphologicalAnalysis=yeni+ADJ}{metaMorphemes=yeni}{semantics=TUR10-0848740}{namedEntity=NONE}{propbank=ARG0$TUR10-0122540} " +
                        "{turkish=Büyük}{morphologicalAnalysis=büyük+ADJ}{metaMorphemes=büyük}{semantics=TUR10-0092410}{namedEntity=NONE}{propbank=ARG0$TUR10-0122540} " +
                        "{turkish=yasada}{morphologicalAnalysis=yasa+NOUN+A3SG+PNON+LOC}{metaMorphemes=yasa+DA}{semantics=TUR10-0411070}{namedEntity=NONE}{propbank=ARG0$TUR10-0122540} " +
                        "{turkish=karmaşık}{morphologicalAnalysis=karmaşık+ADJ}{metaMorphemes=karmaşık}{semantics=TUR10-0422250}{namedEntity=NONE}{propbank=ARG0$TUR10-0122540} " +
                        "{turkish=dil}{morphologicalAnalysis=dil+NOUN+A3SG+PNON+NOM}{metaMorphemes=dil}{semantics=TUR10-0204790}{namedEntity=NONE}{propbank=ARG0$TUR10-0122540} " +
                        "{turkish=savaşı}{morphologicalAnalysis=savaş+NOUN+A3SG+PNON+ACC}{metaMorphemes=savaş+yH}{semantics=TUR10-0135880}{namedEntity=NONE}{propbank=ARG1$TUR10-0122540} " +
                        "{turkish=bulandırmıştır}{morphologicalAnalysis=bulan+VERB^DB+VERB+CAUS+POS+NARR+COP+A3SG}{metaMorphemes=bulan+DHr+mHs+DHr}{semantics=TUR10-0122540}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0122540} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}{propbank=NONE}",
                tree0.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Yollar}{morphologicalAnalysis=yol+NOUN+A3PL+PNON+NOM}{metaMorphemes=yol+lAr}{semantics=TUR10-0858630}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0834430} " +
                        "{turkish=ve}{morphologicalAnalysis=ve+CONJ}{metaMorphemes=ve}{semantics=TUR10-0816400}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0834430} " +
                        "{turkish=Araçlar}{morphologicalAnalysis=araç+NOUN+PROP+A3PL+PNON+NOM}{metaMorphemes=araç+lAr}{semantics=TUR10-0568960}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0834430} " +
                        "{turkish=Komitesi}{morphologicalAnalysis=komite+NOUN+A3SG+P3SG+NOM}{metaMorphemes=komite+sH}{semantics=TUR10-0246850}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0834430} " +
                        "{turkish=gelecek}{morphologicalAnalysis=gelecek+NOUN+A3SG+PNON+NOM}{metaMorphemes=gelecek}{semantics=TUR10-0055940}{namedEntity=NONE}{propbank=NONE} " +
                        "{turkish=Salı}{morphologicalAnalysis=salı+NOUN+A3SG+PNON+NOM}{metaMorphemes=salı}{semantics=TUR10-0660160}{namedEntity=TIME}{propbank=NONE} " +
                        "{turkish=fatura}{morphologicalAnalysis=fatura+NOUN+A3SG+PNON+NOM}{metaMorphemes=fatura}{semantics=TUR10-0265970}{namedEntity=NONE}{propbank=ARGMPNC$TUR10-0834430} " +
                        "{turkish=için}{morphologicalAnalysis=için+POSTP+PCNOM}{metaMorphemes=için}{semantics=TUR10-0811730}{namedEntity=NONE}{propbank=ARGMPNC$TUR10-0834430} " +
                        "{turkish=bir}{morphologicalAnalysis=bir+DET}{metaMorphemes=bir}{semantics=TUR10-0105580}{namedEntity=NONE}{propbank=ARG1$TUR10-0834430} " +
                        "{turkish=duruşma}{morphologicalAnalysis=duruşma+NOUN+A3SG+PNON+NOM}{metaMorphemes=duruşma}{semantics=TUR10-0518340}{namedEntity=NONE}{propbank=ARG1$TUR10-0834430} " +
                        "{turkish=yapacak}{morphologicalAnalysis=yap+VERB+POS+FUT+A3SG}{metaMorphemes=yap+yAcAk}{semantics=TUR10-0834430}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0834430} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}{propbank=NONE}",
                tree1.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Biz}{morphologicalAnalysis=biz+PRON+PERS+A1PL+PNON+NOM}{metaMorphemes=biz}{semantics=TUR10-0110290}{namedEntity=NONE} " +
                        "{turkish=reklamın}{morphologicalAnalysis=reklam+NOUN+A3SG+PNON+GEN}{metaMorphemes=reklam+nHn}{semantics=TUR10-0644960}{namedEntity=NONE} " +
                        "{turkish=işe}{morphologicalAnalysis=iş+NOUN+A3SG+PNON+DAT}{metaMorphemes=iş+yA}{semantics=TUR10-0895120}{namedEntity=NONE} " +
                        "{turkish=yarayıp}{morphologicalAnalysis=yara+VERB+POS^DB+ADV+AFTERDOINGSO}{metaMorphemes=yara+yHp}{semantics=TUR10-0895120}{namedEntity=NONE} " +
                        "{turkish=yaramadığını}{morphologicalAnalysis=yara+VERB+NEG^DB+NOUN+PASTPART+A3SG+P3SG+ACC}{metaMorphemes=yara+mA+DHk+sH+nH}{semantics=TUR10-0835450}{namedEntity=NONE} " +
                        "{turkish=görmek}{morphologicalAnalysis=gör+VERB+POS^DB+NOUN+INF+A3SG+PNON+NOM}{metaMorphemes=gör+mAk}{semantics=TUR10-0305610}{namedEntity=NONE} " +
                        "{turkish=üzereyiz}{morphologicalAnalysis=üzere+POSTP+PCNOM^DB+VERB+ZERO+PRES+A1PL}{metaMorphemes=üzere+yHz}{semantics=TUR10-0811600}{namedEntity=NONE}{propbank=NONE} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}",
                tree2.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Bu}{morphologicalAnalysis=bu+DET}{metaMorphemes=bu}{semantics=TUR10-0120750}{namedEntity=NONE}{propbank=ARGMTMP$TUR10-0328250} " +
                        "{turkish=kez}{morphologicalAnalysis=kez+NOUN+A3SG+PNON+NOM}{metaMorphemes=kez}{semantics=TUR10-0186930}{namedEntity=NONE}{propbank=ARGMTMP$TUR10-0328250} " +
                        "{turkish=,}{morphologicalAnalysis=,+PUNC}{metaMorphemes=,}{semantics=TUR10-0820240}{namedEntity=NONE}{propbank=NONE} " +
                        "{turkish=onlar}{morphologicalAnalysis=o+PRON+PERS+A3PL+PNON+NOM}{metaMorphemes=onlar}{semantics=TUR10-0588290}{namedEntity=NONE}{propbank=ARG0$TUR10-0328250} " +
                        "{turkish=daha}{morphologicalAnalysis=daha+ADV}{metaMorphemes=daha}{semantics=TUR10-0995340}{namedEntity=NONE}{propbank=ARGMMNR$TUR10-0328250} " +
                        "{turkish=da}{morphologicalAnalysis=da+CONJ}{metaMorphemes=da}{semantics=TUR10-0995340}{namedEntity=NONE}{propbank=ARGMMNR$TUR10-0328250} " +
                        "{turkish=hızlı}{morphologicalAnalysis=hız+NOUN+A3SG+PNON+NOM^DB+ADJ+WITH}{metaMorphemes=hız+lH}{semantics=TUR10-1147380}{namedEntity=NONE}{propbank=ARGMMNR$TUR10-0328250} " +
                        "{turkish=hareket}{morphologicalAnalysis=hareket+NOUN+A3SG+PNON+NOM}{metaMorphemes=hareket}{semantics=TUR10-0328250}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0328250} " +
                        "{turkish=ediyor}{morphologicalAnalysis=et+VERB+POS+PROG1+A3SG}{metaMorphemes=et+Hyor}{semantics=TUR10-0328250}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0328250} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}{propbank=NONE}",
                tree3.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Shearson}{morphologicalAnalysis=shearson+NOUN+PROP+A3SG+PNON+NOM}{metaMorphemes=shearson}{semantics=TUR10-0000000}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0845200} " +
                        "{turkish=Lehman}{morphologicalAnalysis=lehman+NOUN+PROP+A3SG+PNON+NOM}{metaMorphemes=lehman}{semantics=TUR10-0000000}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0845200} " +
                        "{turkish=Hutton}{morphologicalAnalysis=hutton+NOUN+PROP+A3SG+PNON+NOM}{metaMorphemes=hutton}{semantics=TUR10-0000000}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0845200} " +
                        "{turkish=Şirketi}{morphologicalAnalysis=şirket+NOUN+A3SG+PNON+ACC}{metaMorphemes=şirket+yH}{semantics=TUR10-0732210}{namedEntity=ORGANIZATION}{propbank=ARG0$TUR10-0845200} " +
                        "{turkish=dün}{morphologicalAnalysis=dün+NOUN+A3SG+PNON+NOM}{metaMorphemes=dün}{semantics=TUR10-0229160}{namedEntity=NONE}{propbank=ARGMTMP$TUR10-0845200} " +
                        "{turkish=sabaha}{morphologicalAnalysis=sabah+NOUN+A3SG+PNON+DAT}{metaMorphemes=sabah+yA}{semantics=TUR10-1124640}{namedEntity=NONE}{propbank=ARGMTMP$TUR10-0845200} " +
                        "{turkish=kadar}{morphologicalAnalysis=kadar+POSTP+PCDAT}{metaMorphemes=kadar}{semantics=TUR10-1124640}{namedEntity=NONE}{propbank=ARGMTMP$TUR10-0845200} " +
                        "{turkish=çoktan}{morphologicalAnalysis=çoktan+ADV}{metaMorphemes=çoktan}{semantics=TUR10-0606290}{namedEntity=NONE} " +
                        "{turkish=yeni}{morphologicalAnalysis=yeni+ADJ}{metaMorphemes=yeni}{semantics=TUR10-0848690}{namedEntity=NONE}{propbank=ARG1$TUR10-0845200} " +
                        "{turkish=televizyon}{morphologicalAnalysis=televizyon+NOUN+A3SG+PNON+NOM}{metaMorphemes=televizyon}{semantics=TUR10-0761090}{namedEntity=NONE}{propbank=ARG1$TUR10-0845200} " +
                        "{turkish=reklamlarını}{morphologicalAnalysis=reklam+NOUN+A3PL+P3SG+ACC}{metaMorphemes=reklam+lAr+sH+nH}{semantics=TUR10-0644970}{namedEntity=NONE}{propbank=ARG1$TUR10-0845200} " +
                        "{turkish=yazmıştı}{morphologicalAnalysis=yaz+VERB+POS+NARR+PAST+A3SG}{metaMorphemes=yaz+mHs+yDH}{semantics=TUR10-0845200}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0845200} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}{propbank=NONE}",
                tree4.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Bu}{morphologicalAnalysis=bu+DET}{metaMorphemes=bu}{semantics=TUR10-0120750}{namedEntity=NONE} " +
                        "{turkish=kez}{morphologicalAnalysis=kez+NOUN+A3SG+PNON+NOM}{metaMorphemes=kez}{semantics=TUR10-0186930}{namedEntity=NONE} " +
                        "{turkish=firmalar}{morphologicalAnalysis=firma+NOUN+A3PL+PNON+NOM}{metaMorphemes=firma+lAr}{semantics=TUR10-0275050}{namedEntity=NONE} " +
                        "{turkish=hazırdı}{morphologicalAnalysis=hazır+ADJ^DB+VERB+ZERO+PAST+A3SG}{metaMorphemes=hazır+yDH}{semantics=TUR10-0031920}{namedEntity=NONE}{propbank=NONE} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}",
                tree5.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Bu}{morphologicalAnalysis=bu+DET}{metaMorphemes=bu}{semantics=TUR10-0120760}{namedEntity=NONE} " +
                        "{turkish=diyaloğu}{morphologicalAnalysis=diyalog+NOUN+A3SG+PNON+ACC}{metaMorphemes=diyalog+yH}{semantics=TUR10-0561260}{namedEntity=NONE} " +
                        "{turkish=sürdürmek}{morphologicalAnalysis=sür+VERB^DB+VERB+CAUS+POS^DB+NOUN+INF+A3SG+PNON+NOM}{metaMorphemes=sür+DHr+mAk}{semantics=TUR10-0405050}{namedEntity=NONE} " +
                        "{turkish=kesinlikle}{morphologicalAnalysis=kesinlikle+ADV}{metaMorphemes=kesinlikle}{semantics=TUR10-0076380}{namedEntity=NONE} " +
                        "{turkish=çok}{morphologicalAnalysis=çok+ADV}{metaMorphemes=çok}{semantics=TUR10-1205910}{namedEntity=NONE} " +
                        "{turkish=önemlidir}{morphologicalAnalysis=önemli+ADJ^DB+VERB+ZERO+PRES+COP+A3SG}{metaMorphemes=önemli+DHr}{semantics=TUR10-1205910}{namedEntity=NONE} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}",
                tree6.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Cuma}{morphologicalAnalysis=cuma+NOUN+A3SG+PNON+NOM}{metaMorphemes=cuma}{semantics=TUR10-0142770}{namedEntity=TIME}{propbank=ARGMTMP$TUR10-0405220} " +
                        "{turkish=günü}{morphologicalAnalysis=gün+NOUN+A3SG+P3SG+NOM}{metaMorphemes=gün+sH}{semantics=TUR10-0314950}{namedEntity=TIME}{propbank=ARGMTMP$TUR10-0405220} " +
                        "{turkish=düşünmek}{morphologicalAnalysis=düşün+VERB+POS^DB+NOUN+INF+A3SG+PNON+NOM}{metaMorphemes=düşün+mAk}{semantics=TUR10-0231150}{namedEntity=NONE}{propbank=ARG0$TUR10-0405220} " +
                        "{turkish=için}{morphologicalAnalysis=için+POSTP+PCNOM}{metaMorphemes=için}{semantics=TUR10-0811730}{namedEntity=NONE}{propbank=ARG0$TUR10-0405220} " +
                        "{turkish=çok}{morphologicalAnalysis=çok+ADV}{metaMorphemes=çok}{semantics=TUR10-0583380}{namedEntity=NONE}{propbank=ARG0$TUR10-0405220} " +
                        "{turkish=geç}{morphologicalAnalysis=geç+ADJ}{metaMorphemes=geç}{semantics=TUR10-0286850}{namedEntity=NONE}{propbank=ARG0$TUR10-0405220} " +
                        "{turkish=olacaktı}{morphologicalAnalysis=ol+VERB+POS+FUT+PAST+A3SG}{metaMorphemes=ol+yAcAk+yDH}{semantics=TUR10-0405220}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0405220} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}{propbank=NONE}",
                tree7.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Bunu}{morphologicalAnalysis=bu+PRON+DEMONSP+A3SG+PNON+ACC}{metaMorphemes=bu+nH}{semantics=TUR10-0120760}{namedEntity=NONE} " +
                        "{turkish=vaktinden}{morphologicalAnalysis=vakit+NOUN+A3SG+P3SG+ABL}{metaMorphemes=vakit+sH+nDAn}{semantics=TUR10-0813160}{namedEntity=NONE} " +
                        "{turkish=önce}{morphologicalAnalysis=önce+ADV}{metaMorphemes=önce}{semantics=TUR10-0602850}{namedEntity=NONE} " +
                        "{turkish=düşünmek}{morphologicalAnalysis=düşün+VERB+POS^DB+NOUN+INF+A3SG+PNON+NOM}{metaMorphemes=düşün+mAk}{semantics=TUR10-0231190}{namedEntity=NONE} " +
                        "{turkish=zorundaydık}{morphologicalAnalysis=zor+ADJ^DB+NOUN+ZERO+A3SG+P3SG+LOC^DB+VERB+ZERO+PAST+A1PL}{metaMorphemes=zor+sH+nDA+yDH+k}{semantics=TUR10-0877200}{namedEntity=NONE} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}",
                tree8.generateAnnotatedSentence().toString());
        assertEquals("{turkish=Bu}{morphologicalAnalysis=bu+PRON+DEMONSP+A3SG+PNON+NOM}{metaMorphemes=bu}{semantics=TUR10-0120760}{namedEntity=NONE}{propbank=ARG0$TUR10-0915630} " +
                        "{turkish=birkaç}{morphologicalAnalysis=birkaç+DET}{metaMorphemes=birkaç}{semantics=TUR10-0096000}{namedEntity=NONE}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=farklı}{morphologicalAnalysis=fark+NOUN+A3SG+PNON+NOM^DB+ADJ+WITH}{metaMorphemes=fark+lH}{semantics=TUR10-0160520}{namedEntity=NONE}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=Fidelity}{morphologicalAnalysis=Fidelity+NOUN+PROP+A3SG+PNON+NOM}{metaMorphemes=Fidelity}{semantics=TUR10-0000000}{namedEntity=ORGANIZATION}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=fonu}{morphologicalAnalysis=fon+NOUN+A3SG+P3SG+NOM}{metaMorphemes=fon+sH}{semantics=TUR10-0277530}{namedEntity=ORGANIZATION}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=adıyla}{morphologicalAnalysis=ad+NOUN+A3SG+P3SG+INS}{metaMorphemes=ad+sH+ylA}{semantics=TUR10-0733660}{namedEntity=NONE}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=reklamını}{morphologicalAnalysis=reklam+NOUN+A3SG+P3SG+ACC}{metaMorphemes=reklam+sH+nH}{semantics=TUR10-1123150}{namedEntity=NONE}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=yapmaya}{morphologicalAnalysis=yap+VERB+POS^DB+NOUN+INF2+A3SG+PNON+DAT}{metaMorphemes=yap+mA+yA}{semantics=TUR10-1123150}{namedEntity=NONE}{propbank=ARG1$TUR10-0915630} " +
                        "{turkish=devam}{morphologicalAnalysis=devam+NOUN+A3SG+PNON+NOM}{metaMorphemes=devam}{semantics=TUR10-0915630}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0915630} " +
                        "{turkish=eder}{morphologicalAnalysis=et+VERB+POS+AOR+A3SG}{metaMorphemes=et+Ar}{semantics=TUR10-0915630}{namedEntity=NONE}{propbank=PREDICATE$TUR10-0915630} " +
                        "{turkish=.}{morphologicalAnalysis=.+PUNC}{metaMorphemes=.}{semantics=TUR10-1081860}{namedEntity=NONE}{propbank=NONE}",
                tree9.generateAnnotatedSentence().toString());
    }

    @Test
    public void testGenerateAnnotatedSentence2() throws Exception {
        ParseTreeDrawable tree = new ParseTreeDrawable(new FileInputStream("trees2/0000.dev"));
        assertEquals("{english=The}{posTag=DT} {english=complicated}{posTag=VBN} {english=language}{posTag=NN} {english=in}{posTag=IN} {english=the}{posTag=DT} {english=huge}{posTag=JJ} {english=new}{posTag=JJ} {english=law}{posTag=NN} {english=has}{posTag=VBZ} {english=muddied}{posTag=VBN} {english=the}{posTag=DT} {english=fight}{posTag=NN} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0001.dev"));
        assertEquals("{english=The}{posTag=DT} {english=Ways}{posTag=NNP} {english=and}{posTag=CC} {english=Means}{posTag=NNP} {english=Committee}{posTag=NNP} {english=will}{posTag=MD} {english=hold}{posTag=VB} {english=a}{posTag=DT} {english=hearing}{posTag=NN} {english=on}{posTag=IN} {english=the}{posTag=DT} {english=bill}{posTag=NN} {english=next}{posTag=IN} {english=Tuesday}{posTag=NNP} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0002.dev"));
        assertEquals("{english=We}{posTag=PRP} {english='re}{posTag=VBP} {english=about}{posTag=IN} {english=to}{posTag=TO} {english=see}{posTag=VB} {english=if}{posTag=IN} {english=advertising}{posTag=NN} {english=works}{posTag=VBZ} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0003.dev"));
        assertEquals("{english=This}{posTag=DT} {english=time}{posTag=NN} {english=around}{posTag=RP} {english=,}{posTag=,} {english=they}{posTag=PRP} {english='re}{posTag=VBP} {english=moving}{posTag=VBG} {english=even}{posTag=RB} {english=faster}{posTag=RBR} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0004.dev"));
        assertEquals("{english=Shearson}{posTag=NNP} {english=Lehman}{posTag=NNP} {english=Hutton}{posTag=NNP} {english=Inc.}{posTag=NNP} {english=by}{posTag=IN} {english=yesterday}{posTag=NN} {english=afternoon}{posTag=NN} {english=had}{posTag=VBD} {english=already}{posTag=RB} {english=written}{posTag=VBN} {english=new}{posTag=JJ} {english=TV}{posTag=NN} {english=ads}{posTag=NNS} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0005.dev"));
        assertEquals("{english=This}{posTag=DT} {english=time}{posTag=NN} {english=,}{posTag=,} {english=the}{posTag=DT} {english=firms}{posTag=NNS} {english=were}{posTag=VBD} {english=ready}{posTag=JJ} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0006.dev"));
        assertEquals("{english=``}{posTag=``} {english=To}{posTag=TO} {english=maintain}{posTag=VB} {english=that}{posTag=DT} {english=dialogue}{posTag=NN} {english=is}{posTag=VBZ} {english=absolutely}{posTag=RB} {english=crucial}{posTag=JJ} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0007.dev"));
        assertEquals("{english=It}{posTag=PRP} {english=would}{posTag=MD} {english=have}{posTag=VB} {english=been}{posTag=VBN} {english=too}{posTag=RB} {english=late}{posTag=JJ} {english=to}{posTag=TO} {english=think}{posTag=VB} {english=about}{posTag=IN} {english=on}{posTag=IN} {english=Friday}{posTag=NNP} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0008.dev"));
        assertEquals("{english=We}{posTag=PRP} {english=had}{posTag=VBD} {english=to}{posTag=TO} {english=think}{posTag=VB} {english=about}{posTag=IN} {english=it}{posTag=PRP} {english=ahead}{posTag=RB} {english=of}{posTag=IN} {english=time}{posTag=NN} {english=.}{posTag=.} {english=''}{posTag=''}",
                tree.generateAnnotatedSentence("english").toString());
        tree = new ParseTreeDrawable(new FileInputStream("trees2/0009.dev"));
        assertEquals("{english=It}{posTag=PRP} {english=goes}{posTag=VBZ} {english=on}{posTag=RB} {english=to}{posTag=TO} {english=plug}{posTag=VB} {english=a}{posTag=DT} {english=few}{posTag=JJ} {english=diversified}{posTag=JJ} {english=Fidelity}{posTag=NNP} {english=funds}{posTag=NNS} {english=by}{posTag=IN} {english=name}{posTag=NN} {english=.}{posTag=.}",
                tree.generateAnnotatedSentence("english").toString());
    }

}