/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 * 
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, Oracle and/or its affiliates
 * All rights reserved.
 */
package com.oracle.truffle.r.test.testrgen;

import org.junit.*;

import com.oracle.truffle.r.test.*;

public class TestrGenBuiltinbacksolve extends TestBase {

	@Test
	public void testbacksolve1(){
		assertEval("argv <- list(structure(c(-2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0421675318334475, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0421675318334475, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.445407110781343, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510819156714975, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510819156714975, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510819156714975, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0592753100948471, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.428419619610855, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.252201198430086, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.428419619610855, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510819156714975, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0592753100948472, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, -0.0592753100948472, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, -0.252201198430086, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, -0.252201198430086, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, -0.0421675318334475, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, -0.0421675318334475, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, -0.0486697428190666, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, -0.0486697428190666, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, -0.0486697428190666, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, -0.0486697428190666, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.935285871700694), .Dim = c(22L, 22L), .Dimnames = list(c(\'StripS01\', \'StripS02\', \'StripS03\', \'StripS04\', \'StripS05\', \'StripS06\', \'StripS07\', \'StripS08\', \'StripS09\', \'StripS10\', \'StripS11\', \'StripS12\', \'StripS13\', \'StripS14\', \'StripS15\', \'StripS16\', \'StripS17\', \'StripS18\', \'StripS19\', \'StripS20\', \'StripS21\', \'\'), c(\'3\', \'4\', \'5\', \'6\', \'9\', \'10\', \'11\', \'12\', \'13\', \'14\', \'15\', \'16\', \'19\', \'20\', \'21\', \'25\', \'26\', \'27\', \'31\', \'32\', \'33\', \'39\'))), structure(c(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.00019677474442243), .Dim = c(22L, 1L)), 22L, FALSE, FALSE); .Internal(backsolve(argv[[1]], argv[[2]], argv[[3]], argv[[4]], argv[[5]]))");
	}

	@Test
	public void testbacksolve2(){
		assertEval("argv <- list(structure(c(-2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0421590411210753, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0421590411210753, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.445373554228914, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510781706167877, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510781706167877, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510781706167877, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0592635069735823, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.428392065749892, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.252172670570357, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.428392065749892, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.510781706167877, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0592635069735823, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, 0, -0.0592635069735823, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, 0, -0.252172670570357, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.4142135623731, 0, 0, 0, 0, 0, 0, -0.252172670570357, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, -0.0421590411210753, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, -0.0421590411210753, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, 0, -0.0486599542810648, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, 0, -0.0486599542810647, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, 0, -0.0486599542810648, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.73205080756888, -0.0486599542810648, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.935253697073914), .Dim = c(22L, 22L), .Dimnames = list(c(\'StripS01\', \'StripS02\', \'StripS03\', \'StripS04\', \'StripS05\', \'StripS06\', \'StripS07\', \'StripS08\', \'StripS09\', \'StripS10\', \'StripS11\', \'StripS12\', \'StripS13\', \'StripS14\', \'StripS15\', \'StripS16\', \'StripS17\', \'StripS18\', \'StripS19\', \'StripS20\', \'StripS21\', \'\'), c(\'3\', \'4\', \'5\', \'6\', \'9\', \'10\', \'11\', \'12\', \'13\', \'14\', \'15\', \'16\', \'19\', \'20\', \'21\', \'25\', \'26\', \'27\', \'31\', \'32\', \'33\', \'39\'))), structure(c(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.20033559004316e-05), .Dim = c(22L, 1L)), 22L, FALSE, FALSE); .Internal(backsolve(argv[[1]], argv[[2]], argv[[3]], argv[[4]], argv[[5]]))");
	}

	@Test
	public void testbacksolve3(){
		assertEval("argv <- list(structure(c(-0.91092349872819, -1.26769315823132, 0, -1.11965595698793), .Dim = c(2L, 2L)), structure(c(-0.000210872744086474, 0.000210873298561107), .Dim = c(2L, 1L)), 2L, FALSE, FALSE); .Internal(backsolve(argv[[1]], argv[[2]], argv[[3]], argv[[4]], argv[[5]]))");
	}
}
