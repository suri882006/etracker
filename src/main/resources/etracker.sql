-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 22, 2017 at 12:51 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `etracker`
--

-- --------------------------------------------------------

--
-- Table structure for table `tblexpenses`
--

CREATE TABLE `tblexpenses` (
  `expenseid` bigint(20) NOT NULL,
  `expensetypeid` int(11) DEFAULT NULL,
  `userid` bigint(20) NOT NULL,
  `expensedescription` varchar(256) DEFAULT NULL,
  `amount` float NOT NULL,
  `date` date NOT NULL,
  `isregular` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblexpensetype`
--

CREATE TABLE `tblexpensetype` (
  `expensetypeid` int(11) NOT NULL,
  `expensename` varchar(32) NOT NULL,
  `expensetype` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbluserlogin`
--

CREATE TABLE `tbluserlogin` (
  `userid` bigint(20) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(10) NOT NULL,
  `lastlogin` date DEFAULT NULL,
  `isdisabled` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblexpenses`
--
ALTER TABLE `tblexpenses`
  ADD PRIMARY KEY (`expenseid`);

--
-- Indexes for table `tblexpensetype`
--
ALTER TABLE `tblexpensetype`
  ADD PRIMARY KEY (`expensetypeid`);

--
-- Indexes for table `tbluserlogin`
--
ALTER TABLE `tbluserlogin`
  ADD PRIMARY KEY (`userid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblexpenses`
--
ALTER TABLE `tblexpenses`
  MODIFY `expenseid` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
--
-- AUTO_INCREMENT for table `tblexpensetype`
--
ALTER TABLE `tblexpensetype`
  MODIFY `expensetypeid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tbluserlogin`
--
ALTER TABLE `tbluserlogin`
  MODIFY `userid` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
