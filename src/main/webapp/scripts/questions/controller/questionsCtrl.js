(function() {
  angular.module('occIDEASApp.Questions')
    .controller('QuestionsCtrl', QuestionsCtrl)
    .filter('reverse', function() {
      return function(items) {
        return items.slice().reverse();
      };
    });

  QuestionsCtrl.$inject = ['data', '$scope', '$mdDialog', 'FragmentsService',
    '$q', 'QuestionsService', 'ModulesService',
    '$anchorScroll', '$location', '$mdMedia', '$window', '$state',
    'AgentsService', 'RulesService', '$compile',
    '$rootScope', 'ModuleRuleService', '$log', '$timeout',
    'AuthenticationService', '$document', 'InterviewsService',
    'SystemPropertyService', 'ngToast', '$translate',
    'NodeLanguageService', '$sessionStorage', 'lang', 'scrollTo', '$http', 'bsLoadingOverlayService'];

  function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService,
                         $q, QuestionsService, ModulesService,
                         $anchorScroll, $location, $mdMedia, $window, $state,
                         AgentsService, RulesService, $compile, $rootScope,
                         ModuleRuleService, $log, $timeout, auth, $document, InterviewsService,
                         SystemPropertyService, ngToast, $translate,
                         NodeLanguageService, $sessionStorage, lang, scrollTo, $http, bsLoadingOverlayService) {
    var self = this;
    self.lang = lang;
    self.data = data;
    var indexCount = 0;
    var totalIndexCount = self.data[0].nodes.length;
    self.determinateValue = indexCount / totalIndexCount;

    self.node = data;

//		self.node = reduceNodeDetails(data);
    function reduceNodeDetails(node) {
      var result = angular.copy(node);
      result[indexCount].nodes = [];
      if(totalIndexCount > 0) {
        result[indexCount].nodes.push(self.data[indexCount].nodes[indexCount]);
      }
      indexCount++;
      return result;
    }

    if(self.lang) {
      $sessionStorage.idNode = self.node[0].idNode;
      $translate.refresh();
      $translate.use(self.lang.language);
      self.translateNode = true;
      self.editTranslateNode = true;
      $scope.selectedLanguage = self.lang;
    }

    if(scrollTo > 0) {
      $scope.$watch(function() {
        return $('#node-' + scrollTo).offset() != null
          && $('#node-' + scrollTo).offset().top
      }, function() {
        if(!$('#' + scrollTo).hasClass('highlight-rulenode')
          && $('#node-' + scrollTo).offset() != null
          && $('#node-' + scrollTo).offset().top != 0) {
          $('#' + scrollTo).addClass('highlight-rulenode');
          $scope.scrollTo('node-' + scrollTo);
        }
      });
    }

    if(self.lang) {
      $scope.flagUsed = 'flag-icon-' + self.lang.flag.split(/[- ]+/).pop().toLowerCase();
      if($sessionStorage.languages) {
        var lang = _.find($sessionStorage.languages, function(o) {
          return o.flag == self.lang.flag;
        });
        $scope.flagDescription = lang.description;
      }
    }

    $scope.languageNameList = [];
    self.editTranslation = function() {
      self.editTranslateNode = true;
    };


    $scope.saveAllLanguage = function() {
      var listOfTranslatedNodes = [];
      var listOfLang = [];
      saveNodeTranslation(angular.copy(self.node[0]), listOfTranslatedNodes, listOfLang);

      NodeLanguageService.batchSave(listOfLang).then(function(response) {
        if(response.status == '200') {
          $translate.refresh();
          $translate.use(self.lang.language);
        }
      });
      self.editTranslateNode = false;
    };

    function saveNodeTranslation(node, listOfTranslatedNodes, listOfLang) {
      if(node.translated != 'No available translation'
        && !listOfTranslatedNodes.indexOf(node.name.toLowerCase().trim()) > -1) {
        listOfTranslatedNodes.push(node.name.toLowerCase().trim());
        var data = {
          languageId: $scope.selectedLanguage.id,
          word: node.name.trim(),
          translation: node.translated
        };
        listOfLang.push(data);
      }
      _.each(node.nodes, function(n) {
        saveNodeTranslation(n, listOfTranslatedNodes, listOfLang);
      });
    }

    $scope.data = data;
    //saveModuleWithoutReload();
    var moduleIdNode = $scope.data[0].idNode;
    $scope.$window = $window;
    $scope.isDragging = false;
    $scope.activeNodeId = 0;
    $anchorScroll.yOffset = 200;
    $scope.rulesObj = [];
    $scope.rulesInt = [];
    $scope.agentsData = null;
    $rootScope.studyModules = [];
    $rootScope.studyModuleLinks = [];
    $rootScope.studyAjsmLinks = [];
    $rootScope.studyAjsmLinksCount = 0;
    $rootScope.studyModuleLinksCount = 0;


    $rootScope.$on('$translateChangeSuccess', function() {
      translateNodes($scope.data[0]);
      ModulesService.getModuleTranslationCurrentCount($scope.data[0].idNode, $scope.selectedLanguage.id).then(function(response) {
        if(response.status == '200') {
          $scope.currentLangCount = response.data;
        }
      });
      ModulesService.getModuleTranslationTotalCount($scope.data[0].idNode).then(function(response) {
        if(response.status == '200') {
          $scope.totalLangCount = response.data;
        }
      });
      $scope.languageNameList.length = 0;
//            refreshNodeLanguageCount($scope.data[0]);
    });

    function translateNodes(node) {
      node.translated = $translate.instant(node.name.toLowerCase());
      if(node.idNode == $scope.data[0].idNode) {
        node.translated = node.name;
      }

      if(node.translated.trim() == node.name.toLowerCase().trim() && node.idNode != $scope.data[0].idNode) {
        node.translated = 'No available translation';
      }
      if(node.nodes) {
        _.each(node.nodes, function(childNode) {
          return translateNodes(childNode);
        });
      }
    }

    var testArray = [];

    function refreshNodeLanguageCount(node) {

      if(node.name && $scope.languageNameList.indexOf(node.name.toLowerCase()) == -1
        && node.link == 0 && node.type != 'P_freetext' &&
        !node.type.match('frequency') && node.nodeclass != 'M' && node.nodeclass != 'F') {
        if(node.translated.trim() != 'No available translation') {
          $scope.currentLangCount++;
        }
        $scope.languageNameList.push(node.name.toLowerCase());
      }
      if(node.nodes) {
        _.each(node.nodes, function(childNode) {
          return refreshNodeLanguageCount(childNode);
        });
      }
    }

    $scope.translateSameNodeName = function(node) {
      checkAndTranslateSameNode($scope.data[0], node.name, node.translated);
    };

    function checkAndTranslateSameNode(node, name, translation) {
      if(node.name.toLowerCase() == name.toLowerCase() && node.link == 0 && node.type != 'P_freetext' && !node.type.match('frequency')) {
        if(translation.trim() != 'No available translation') {
          node.translated = translation;
        }
      }
      if(node.nodes) {
        _.each(node.nodes, function(childNode) {
          return checkAndTranslateSameNode(childNode, name, translation);
        });
      }
    }

    $scope.setBgColor = function(node) {
      if(node.deleted == 1) {
        return {'background-color': 'rgba(255, 208, 208, 0.83)'};
      }
    };

    $scope.$on('QuestionsCtrl:scrollTo', function(event, elId) {
      $scope.scrollWithTimeout(elId);
    });

    $scope.scrollWithTimeout = function(elId) {
      $timeout(function() {
        $scope.highlightNode(elId);
      }, 1000);
    };

    $scope.nodePopover = {
      templateUrl: 'scripts/questions/partials/nodePopover.html',
      open: function(x, idRule) {
        if(x.info) {
          if(x.info["Node" + x.idNode + idRule].nodePopover.isOpen) {
            return;
          }
        }
        var nodeclass = 'P';
        if(angular.isUndefined(x.info)) {
          x.info = [];
        }
        x.info["Node" + x.idNode + idRule] = {
          idNode: x.idNode,
          nodeclass: nodeclass,
          nodePopover: {
            isOpen: false
          },
          nodePopoverInProgress: false
        };
        var nodeInPopup = x.info["Node" + x.idNode + idRule];
        nodeInPopup.nodePopoverInProgress = true;
        var deffered = $q.defer();
        QuestionsService.findPossibleAnswer(nodeInPopup.idNode).then(function(data) {
          nodeInPopup.data = data.data[0];
          nodeInPopup.idRule = idRule;
          nodeInPopup.nodePopoverInProgress = false;
          deffered.resolve();
        });
        deffered.promise.then(function() {
          nodeInPopup.nodePopover.isOpen = true;
        })
      },
      close: function close(x, idRule) {
        x.info["Node" + x.idNode + idRule].nodePopover.isOpen = false;
      }
    };
    $scope.studyAgent = [];

    function initStudyAgent() {
      AgentsService.getStudyAgents().then(function(agent) {
        if(agent) {
          $scope.studyAgent = agent;
        }
      });
    }

    $scope.checkIfStudyAgent = function(idAgent) {
      var retValue = false;
      for(var i = 0; i < $scope.studyAgent.length; i++) {
        var agent = $scope.studyAgent[i];
        if(agent.idAgent == idAgent || agent.agentGroup.name == idAgent) {
          retValue = true;
          break;
        }
      }


      return retValue;
    };

    function initAgentData() {
      initStudyAgent();
      AgentsService.get().then(function(agent) {
        var group = _.groupBy(agent, function(b) {
          return b.agentGroup.name;
        });
        if($scope.data[0].moduleRule) {
          _.forOwn(group, function(x, key) {
            var totalVal = 0;
            _.forEach(x, function(v, k) {
              var ruleArray = _.filter($scope.data[0].moduleRule, function(r) {
                return v.idAgent === r.idAgent;
              });
              var uniqueArray = _.map(_.groupBy(ruleArray, function(item) {
                return item.rule.idRule;
              }), function(grouped) {
                return grouped[0];
              });
              v.total = uniqueArray.length;
              totalVal = totalVal + v.total;
            });
            x.total = totalVal;
          });
        }
        group = setOrder(group);
        $scope.agentsLoading = false;
        $scope.agentsData = group;
        safeDigest($scope.agentsData);
        $log.info("Agent slider has been reloaded ........");
      });
    }

    function initFragmentData() {
      $scope.fragmentsLoading = true;
      FragmentsService.get().then(function(data) {
        var object = {};
        var ajsms = [];
        var templates = [];
        var frequencies = [];
        for(var i = 0; i < data.length; i++) {
          var node = data[i];
          //node.idNode = "";
          node.nodeclass = "Q";
          if(node.type == 'F_ajsm') {
            node.type = "Q_linkedajsm";
            ajsms.push(node);
          } else if(node.type == 'F_template') {
            templates.push(node);
          } else if(node.type == 'F_frequency') {
            frequencies.push(node);
          }
        }
        $scope.templateData = templates;
        $scope.frequencyData = frequencies;
        $scope.aJSMData = ajsms;
        $scope.fragmentsLoading = false;
        $log.info("Fragment slider has been reloaded....");
        return object;
      });
    }

    function setOrder(obj) {
      var out = [];
      _.forEach(obj, function(value, key) {
        out.push({key: key, value: value, total: value.total});
      });
      return out;
    }

    $scope.toggleRulesObj = function(agents) {
      if(_.findIndex($scope.rulesObj, function(o) {
        return o.idAgent === agents.idAgent;
      }) != -1) {
        $scope.safeApply(function() {
          $scope.rulesObj.splice(_.findIndex($scope.rulesObj, function(o) {
            return o.idAgent === agents.idAgent;
          }), 1)[0];
        });
        agents.style = "";
        var dialogRules = angular.element(".note");
        _.each(dialogRules, function(e) {
          if(_.includes(e.id, '-' + agents.idAgent + '-')) {
            e.remove();
          }
        })
      } else {
        $scope.rulesObj.push(agents);
        agents.style = "agent-shown";
      }

    };
    $scope.getRulesIfAny = function(node, agents) {
      var filteredAgent = _.filter(node.moduleRule, _.matches({'idAgent': agents.idAgent}));
      if(filteredAgent.length > 0) {
        return filteredAgent;
      }
      return null;
    };

    $scope.topDirections = ['left', 'up'];
    $scope.bottomDirections = ['down', 'right'];
    self.isOpen = false;
    $scope.availableModes = ['md-fling', 'md-scale'];
    $scope.selectedMode = 'md-scale';
    $scope.availableDirections = ['up', 'down', 'left', 'right'];
    $scope.selectedDirection = 'left';

    $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');

    $window.beforeNode = [];
    $window.afterNode = [];
    $scope.undoEnable = false;

    $scope.isCollapsableNode = function(node) {
      if(node) {
        if(((node.nodeclass === 'M') || (node.nodeclass === 'F'))) {
          return false;
        } else if(node.nodes.length == 0) {
          return false;
        } else {
          return true;
        }
      } else {
        return false;
      }
    };
    $scope.isModuleHeaderNode = function(node) {
      if(node) {
        if(node.type.indexOf('M_Module') > -1) {
          return true;
        } else if(node.type.indexOf('M_Module_') > -1) {
          return true;
        } else if(node.type.indexOf('M_Module__') > -1) {
          return true;
        } else if(node.type.indexOf('M_IntroModule') > -1) {
          return true;
        } else if(node.type.indexOf('F_template') > -1) {
          return true;
        } else if(node.type.indexOf('F_ajsm') > -1) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    };

    $scope.setNodeType = function(node, type) {
      if(type == 'Prod') {
        if(node.nodeclass == 'M') {
          node.type = 'M_Module';
        }
      } else if(type == 'Dev') {
        if(node.nodeclass == 'M') {
          node.type = 'M_Module_';
        }
      } else if(type == 'Test') {
        if(node.nodeclass == 'M') {
          node.type = 'M_Module__';
        }
      } else if(type == 'Intro') {
        if(node.nodeclass == 'M') {
          node.type = 'M_IntroModule';
        }
      }
      saveModuleWithoutReload();
    };
    $scope.setRuleType = function(rule, type) {
      rule.type = type;
      if((type == 'NOISE') || (type == 'BACKGROUND')) {
        if(rule.ruleAdditionalfields == null) {
          rule.ruleAdditionalfields = [];
          rule.ruleAdditionalfields.push(
            {
              idRule: rule.idRule,
              value: '',
              additionalfield: {
                idadditionalfield: 1,
                type: 'NOISE_Db',
                value: ''
              }
            });

        } else if(rule.ruleAdditionalfields.length == 0) {
          rule.ruleAdditionalfields.push(
            {
              idRule: rule.idRule,
              value: '',
              additionalfield: {
                idadditionalfield: 1,
                type: 'NOISE_Db',
                value: ''
              }
            });

        }
      } else if((type == 'VIBRATION')) {
        if(rule.ruleAdditionalfields == null) {
          rule.ruleAdditionalfields = [];
          rule.ruleAdditionalfields.push(
            {
              idRule: rule.idRule,
              value: '',
              additionalfield: {
                idadditionalfield: 3,
                type: 'VIBRATION_level',
                value: ''
              }
            });

        } else if(rule.ruleAdditionalfields.length == 0) {
          rule.ruleAdditionalfields.push(
            {
              idRule: rule.idRule,
              value: '',
              additionalfield: {
                idadditionalfield: 3,
                type: 'VIBRATION_level',
                value: ''
              }
            });

        }
      }
    };

    $scope.aJsmTreeOptions = {
      accept: function(sourceNodeScope, destNodesScope, destIndex) {

        return true;
      },
      dragStart: function(event) {
        $scope.undoEnable = false;
      },
      beforeDrag: function(sourceNodeScope) {
        $scope.isDragging = true;

        return true;
      },
      beforeDrop: function(event) {
        var sourceNode = event.source.nodeScope.node;
        var destNode = event.dest.nodesScope.node;
        $log.info("source" + sourceNode.type);


        if(!destNode) {
          $scope.isDragging = false;
          return false;
        } else {
          if(sourceNode.nodeclass == 'Q') {
            if(destNode.nodeclass == 'Q') {
              sourceNode.warning = '';
              $scope.isDragging = false;
              return false;
            }
          } else if(sourceNode.nodeclass == 'P') {
            if(destNode.nodeclass == 'P') {
              sourceNode.warning = '';
              $scope.isDragging = false;
              return false;
            } else if(destNode.nodeclass == 'M') {
              sourceNode.warning = '';
              $scope.isDragging = false;
              return false;
            }
          }

          /*destNode.nodes.unshift({

								name : sourceNode.name,
								description : sourceNode.description,
								topNodeId : sourceNode.idNode,
								type : sourceNode.type,
								nodeclass : sourceNode.nodeclass,
								link : sourceNode.idNode,
								parentId : destNode.idNode,
								nodes : []
						});*/

          //reorderSequence(destNode.nodes);
          //saveModuleAndReload();

          return true;
        }

      },
      dropped: function(event) {
        $scope.isDragging = false;
        var sourceNode = event.source.nodeScope.node;
        sourceNode.warning = null;
        var destNode = event.dest.nodesScope.node;

        $log.info("source" + sourceNode.type);
        if(!destNode) {
          $log.warning("Node is dropped on the wrong spot -"
            + ": source" + sourceNode + "dest:" + destNode);
        } else {
          $log.info("dest " + destNode.type);

        }
        reorderSequence($scope.data);
        saveModuleWithoutReload();
      }
    };
    $scope.templateTreeOptions = {
      accept: function(sourceNodeScope, destNodesScope, destIndex) {

        return true;
      },
      dragStart: function(event) {
        $scope.undoEnable = false;
      },
      beforeDrop: function(event) {
        var sourceNode = event.source.nodeScope.node;
        var destNode = event.dest.nodesScope.node;
        $log.info("source" + sourceNode.type);
        if(!destNode) {
          $scope.isDragging = false;
          return false;
        }
        if(sourceNode.nodeclass == 'Q') {
          if(destNode.nodeclass == 'Q') {
            sourceNode.warning = '';
            $scope.isDragging = false;
            return false;
          }
        } else if(sourceNode.nodeclass == 'P') {
          if(destNode.nodeclass == 'P') {
            sourceNode.warning = '';
            $scope.isDragging = false;
            return false;
          } else if(destNode.nodeclass == 'M') {
            sourceNode.warning = '';
            $scope.isDragging = false;
            return false;
          }
        }

        $scope.isDragging = false;
        var deferred = $q.defer();

        return FragmentsService.findFragmentChildNodes(sourceNode.idNode).then(function(fragmentData) {

          cascadeTemplateNullIds(fragmentData);

          destNode.nodes.unshift({

            name: fragmentData[0].name,
            description: fragmentData[0].description,
            type: fragmentData[0].type,
            nodeclass: fragmentData[0].nodeclass,
            nodes: fragmentData[0].nodes
          });
          reorderSequence($scope.data);
          saveModuleWithoutReload();
          deferred.resolve();
          return false;
        });
      },
      beforeDrag: function(sourceNodeScope) {
        $scope.isDragging = true;

        return true;
      },
      dropped: function(event) {
      }
    };
    $scope.moduleTreeOptions = {
      accept: function(sourceNodeScope, destNodesScope, destIndex) {
        var sourceNode = sourceNodeScope.node;
        var destNode = destNodesScope.node;
        var placeholder = document.getElementsByClassName("angular-ui-tree-placeholder");
        var wrappedplaceholder = angular.element(placeholder);
        if(!destNode) {
          sourceNode.warning = 'warning';
        } else {
          if(sourceNode.nodeclass == 'Q' && sourceNode.type != 'Q_linkedmodule') {
            if(destNode.nodeclass == 'Q') {
              $log.info("Hovering Q on Q");

              wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = 'warning';
            } else {
              wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = '';
            }
          } else if(sourceNode.nodeclass == 'Q' && sourceNode.type == 'Q_linkedmodule') {
            if(destNode.nodeclass != 'P') {
              $log.info("Hovering Q_linkedmodule on a non P node");

              wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = 'warning';
            } else {
              wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = '';
            }
          } else if(sourceNode.nodeclass == 'P') {
            if(destNode.nodeclass == 'P') {
              $log.info("Hovering P on P");
              wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = 'warning';
            } else if(destNode.nodeclass == 'M') {
              $log.info("Hovering P on M");
              wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = 'warning';
            } else {
              wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
              sourceNode.warning = '';
            }
          } else {
            wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
            sourceNode.warning = '';
          }
        }
        return true;
      },
      beforeDrop: function(event) {
        var sourceNode = event.source.nodeScope.node;
        var destNode = event.dest.nodesScope.node;
        //if(event.source.index != event.dest.index){
        recordAction($scope.data);
        //}
        $scope.isDragging = false;
        var retValue = true;
        if(!destNode) {
          $scope.isDragging = false;
          retValue = false;
          sourceNode.warning = '';
        } else {
          if(sourceNode.nodeclass == 'Q') {
            if(destNode.nodeclass == 'Q') {
              $log.info("dropped Q on Q");
              retValue = false;
            } else if(sourceNode.type == 'Q_linkedmodule') {
              if(destNode.nodeclass != 'P') {
                $log.info("dropped Q_linkedmodule on a non P node");
                retValue = false;
              }
            }
          } else if(sourceNode.nodeclass == 'P') {
            if(destNode.nodeclass == 'P') {
              $log.info("Dropped P on P");
              retValue = false;
            } else if(destNode.nodeclass == 'M') {
              $log.info("Dropped P on M");
              retValue = false;
            } else {
              sourceNode.warning = '';
            }
          } else {
            sourceNode.warning = '';
          }
        }
        return retValue;
      },
      beforeDrag: function(sourceNodeScope) {
        $scope.isDragging = true;
        if(sourceNodeScope.node.classtype === 'M') {
          $scope.isDragging = false;
          return false;
        } else if(sourceNodeScope.node.classtype === 'F') {
          $scope.isDragging = false;
          return false;
        } else {
          return true;
        }
      },
      dragStop: function(event) {

      },
      dropped: function(event) {

        var sourceNode = event.source.nodeScope.node;
        sourceNode.warning = null;
        var destNode = event.dest.nodesScope.node;
        if($scope.isClonable) {
          $log.info("Just cloned so turning undo off ");
          $scope.undoEnable = false;
        }
        $log.info("source" + sourceNode.type);
        if(!destNode) {
          sourceNode.warning = 'warning';
          $log.warning("Node is dropped on the wrong spot -"
            + ": source" + sourceNode + "dest:" + destNode);
        } else {
          $log.info("dest " + destNode.type);
        }
        $scope.isDragging = false;
        if(sourceNode.warning != 'warning') {
          if($scope.isClonable) {
            $scope.isClonable = false;
            safeDigest($scope.isClonable);
            reorderSequence($scope.data);
            saveModuleWithoutReload();
            event.source.nodeScope.$treeScope.cloneEnabled = false;
            updateRuleDialogIfExist(sourceNode);
          } else {
            saveModuleWithoutReload();
            updateRuleDialogIfExist(sourceNode);
          }
        }
      }
    };


    function updateRuleDialogIfExist(sourceNode) {
      if(sourceNode.moduleRule && sourceNode.moduleRule.length > 0) {
        var ruleDialogId = sourceNode.idNode + '-' + sourceNode.moduleRule[0].idAgent + '-'
          + sourceNode.moduleRule[0].rule.idRule;
        if(angular.element("#" + ruleDialogId)) {
          angular.element("#" + ruleDialogId).remove();
        }
      }
    }

    function reorderSequence(arrayList) {
      var seq = 0;
      _.each(arrayList, function(data) {
        data.sequence = seq;
        seq++;
        if(data.nodes.length > 0) {
          reorderSequence(data.nodes);
        }
      });
      $log.info("reorderSequence:" + data);
    }

    function cascadeTemplateNullIds(nodes) {
      var seq = 1;
      var parentId = parentId;
      _.each(nodes, function(data) {
        data.sequence = seq++;

        data.originalId = data.idNode;
        data.idNode = null;
        data.parentId = null;

        if(data.nodes) {
          if(data.nodes.length > 0) {
            cascadeTemplateNullIds(data.nodes);
          }
        } else {
          data.nodes = [];
        }

      });
      $log.info("cascadeTemplateNullIds:" + nodes);
    }


    $scope.rightNav = "slideFrag";
    $scope.toggleRight = function() {
      if($scope.rightNav === "slideFrag") {
        $scope.rightNav = "";
      } else {
        $scope.rightNav = "slideFrag";
      }
      if(data[0].type != 'M_IntroModule' && (angular.isUndefined($scope.templateData) || angular.isUndefined($scope.aJSMData)
        || angular.isUndefined($scope.frequencyData))) {
        initFragmentData();
      } else {
        ModulesService.getActiveModules().then(function(data) {
          for(var i = 0; i < data.length; i++) {
            var node = data[i];
            node.type = "Q_linkedmodule";
            node.nodeclass = "Q";
          }
          $scope.moduleSlider = data;
        });
      }
    };

    $scope.leftNav = "slideFragLeft";
    $scope.toggleLeft = function() {
      if($scope.leftNav === "slideFragLeft") {
        $scope.leftNav = "";
      } else {
        $scope.leftNav = "slideFragLeft";
      }
      if((angular.isUndefined($scope.agentsData)) || ($scope.agentsData == null)) {
        $scope.agentsLoading = true;
        initAgentData();
      }
    };

    $scope.toggle = function(scope) {
      scope.toggle();
    };

    $scope.toggleMultipleChoice = function(scope) {
      recordAction($scope.data);

      var nodeExists = false;
      $scope.interviewExist = false;
      $scope.validationInProgress = true;

      if(scope.$modelValue) {

        $mdDialog.show({
          scope: $scope,
          preserveScope: true,
          templateUrl: 'scripts/questions/partials/validateChangeDialog.html',
          clickOutsideToClose: false
        });
        //Check if node exists in interview
        InterviewsService.findQuestionsByNodeId(scope.$modelValue.idNode).then(function(response) {
          if(response.status == 200) {
            $scope.validationInProgress = false;
            if(response.data.length > 0) {
              $scope.interviewExist = true;
              $scope.nodesForUpdate = response.data;
              $scope.nodeScope = scope;
            } else {

              //Proceed, no interviews to be updated
              updateScope(scope);
            }
          }
        });
      }
    };

    $scope.cancelChanges = function() {
      $mdDialog.cancel();
    };

    $scope.updateInterviewNodes = function() {

      if($scope.nodesForUpdate) {

        //Update the type
        _.forEach($scope.nodesForUpdate, function(node) {
          if(node.type == 'Q_multiple') {
            node.type = 'Q_single';
          } else {
            node.type = 'Q_multiple';
          }
        });

        InterviewsService.saveQuestions($scope.nodesForUpdate)
          .then(function(response) {
            if(response.status == 200) {
              console.log("Node type update done.");
            }
          });

        if($scope.nodeScope) {
          updateScope($scope.nodeScope);
        }
      }
    };

    function updateScope(scope) {
      if(scope.$modelValue.type == 'Q_multiple') {
        scope.$modelValue.type = 'Q_single';
      } else {
        scope.$modelValue.type = 'Q_multiple';
      }
      saveModuleWithoutReload();
      $mdDialog.cancel();
    }

    $scope.deleteNode = function(scope) {
      if(!checkIfRulesExist(scope.$modelValue)) {
        recordAction($scope.data);
        if(scope.$modelValue.deleted) {
          scope.$modelValue.deleted = 0;
          cascadeDelete(scope.$modelValue.nodes, 0);
        } else {
          scope.$modelValue.deleted = 1;
          cascadeDelete(scope.$modelValue.nodes, 1);
        }

        var deffered = $q.defer();
        saveModuleWithoutReload('', deffered);
        deffered.promise.then(function(resolve) {
          saveModuleWithoutReload();
          searchAndRemoveNode($scope.data, scope);
        });
      }
    };

    function checkIfRulesExist(node) {
      if(node.moduleRule && node.moduleRule.length > 0) {
        var rules = node.moduleRule;
        var msg = "This node (" + node.number + ") has rules on agents ";
        for(var i = 0; i < rules.length; i++) {
          msg += rules[i].agentName + " ";
        }
        msg += "please remove the rules first";
        ngToast.create({
          className: 'warning',
          content: msg,
          dismissOnTimeout: false,
          dismissButton: true
        });
        return true;
      }

      if(node.nodes) {
        for(var i in node.nodes) {
          if(checkIfRulesExist(node.nodes[i])) {
            return true;
          }
        }
      }
    }

    function checkAnswerIfRulesExist(scope) {
      if(scope.$modelValue.moduleRule && scope.$modelValue.moduleRule.length > 0) {
        var rules = scope.$modelValue.moduleRule;
        var msg = "This node (" + scope.$modelValue.number + ") has rules on agents ";
        for(var i = 0; i < rules.length; i++) {
          msg += rules[i].agentName + " ";
        }
        msg += "please remove the rules first";
        ngToast.create({
          className: 'warning',
          content: msg,
          dismissOnTimeout: false,
          dismissButton: true
        });
      } else {
        recordAction($scope.data);
        if(scope.$modelValue.deleted) {
          scope.$modelValue.deleted = 0;
          cascadeDelete(scope.$modelValue.nodes, 0);
        } else {
          scope.$modelValue.deleted = 1;
          cascadeDelete(scope.$modelValue.nodes, 1);
        }

        var deffered = $q.defer();
        saveModuleWithoutReload('', deffered);
        deffered.promise.then(function(resolve) {
          saveModuleWithoutReload();
          searchAndRemoveNode($scope.data, scope);
        });
      }
    }

    function searchAndRemoveNode(objSearch, scope) {
      var index = _.findIndex(objSearch, function(o) {
        if(o.$$hashKey === scope.$modelValue.$$hashKey) {
          return o.number;
        }
        if(!angular.isUndefined(o.nodes) && o.nodes.length > 0) {
          var x = searchAndRemoveNode(o.nodes, scope);
          if(x > -1) {
            o.nodes.splice(x, 1)[0];

          }
        }
      });
      return index;
    }

    function cascadeIdCleanse(arrayInp) {
      if(arrayInp.length > 0) {
        _.each(arrayInp, function(obj) {
          _.each(obj, function(value, key) {
            if(key === 'idNode') {
              obj[key] = "";
            }
          });
          if(obj.nodes.length > 0) {
            cascadeIdCleanse(obj.nodes);
          }
        });

      }
    }

    function cascadeDelete(arrayInp, deleteFlag) {
      if(arrayInp.length > 0) {
        _.each(arrayInp, function(obj) {
          _.each(obj, function(value, key) {
            if(key === 'deleted') {
              obj[key] = deleteFlag;
            }
          });
          if(obj.nodes.length > 0) {
            cascadeDelete(obj.nodes, deleteFlag);
          }
        });

      }
    }


    $scope.moveLastToTheBeginning = function() {
      var a = $scope.data.pop();
      $scope.data.splice(0, 0, a);
    };
    $scope.getNodeId = function(scope) {
      var nodeId = 0;
      if(scope.anchorId) {
        nodeId = scope.anchorId;
      } else {
        nodeId = scope.idNode;
      }
    };
    $scope.newSubItem = function(scope) {
      $scope.undoEnable = false;
      var nodeData = scope.$modelValue;
      var locationId = nodeData.idNode * 10 + nodeData.nodes.length;
      if(!nodeData.nodes) {
        nodeData.nodes = [];
      }
      if(nodeData.type == 'P_simple') {
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          description: "default",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_single",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type == 'Q_single') {
        var type = "P_simple";
        var defaultText = "New Possible Answer";
        if(scope.isFreeText) {
          type = "P_freetext";
          defaultText = "[Freetext]";
        }
        nodeData.nodes.push({
          anchorId: locationId,
          name: defaultText,
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: type,
          nodeclass: "P",
          nodes: []
        });
      } else if(nodeData.type == 'Q_multiple') {
        var type = "P_simple";
        var defaultText = "New Possible Answer";
        if(scope.isFreeText) {
          type = "P_freetext";
          defaultText = "[Freetext]";
        }
        nodeData.nodes.push({
          anchorId: locationId,
          name: defaultText,
          placeholder: "New Multi Possible Answer",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: type,
          nodeclass: "P",
          nodes: []
        });
      } else if(nodeData.type == 'Q_simple') {
        var type = "P_simple";
        var defaultText = "New Possible Answer";
        if(scope.isFreeText) {
          type = "P_freetext";
          defaultText = "[Freetext]";
        }
        nodeData.nodes.push({
          anchorId: locationId,
          name: defaultText,
          placeholder: defaultText,
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: type,
          nodeclass: "P",
          nodes: []
        });
      } else if(nodeData.type == 'M_Module') {
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_simple",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type == 'M_Module_') {
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_simple",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type == 'M_Module__') {
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_simple",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type == 'M_IntroModule') {
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_simple",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type == 'F_ajsm') {
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_simple",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type.indexOf('P') == 0) { //P is first character
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Question",
          placeholder: "New Question",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "Q_simple",
          nodeclass: "Q",
          nodes: []
        });
      } else if(nodeData.type.indexOf('Q') == 0) { //Q is first character
        nodeData.nodes.push({
          anchorId: locationId,
          name: "New Possible Answer",
          placeholder: "New Possible Answer",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "P_simple",
          nodeclass: "P",
          nodes: []
        });
      } else {
        var nodeData = scope.$modelValue;
        nodeData.nodes.push({
          anchorId: locationId,
          name: "new default node",
          placeholder: "new node",
          topNodeId: nodeData.idNode,
          parentId: nodeData.idNode,
          type: "default",
          warning: "warning",
          nodes: []
        });
      }
      //reorderSequence(scope.$modelValue.nodes);
      saveModuleWithoutReload(locationId);
    };

    $scope.collapseAll = function() {
      $scope.$broadcast('collapseAll');
    };

    $scope.expandAll = function() {
      $scope.$broadcast('expandAll');
    };

    $scope.saveEdit = function(scope) {
      if(scope.$modelValue.type == 'P_freetext') {
        if(!contains(scope.$modelValue.name, 'freetext')) {
          ngToast.create({
            className: 'danger',
            content: "this answer should have the word freetext, adding it for your convenience.",
            animation: 'slide'
          });
          scope.$modelValue.name = scope.$modelValue.name + " [Freetext]"
        }
      }

      if(!scope.$modelValue.name) {
        scope.$modelValue.name = 'Blank';
      }
      if(!scope.$modelValue.number) {
        scope.$modelValue.nodeclass = 'Q';
      }
      $scope.safeApply(function() {
        scope.$modelValue.editEnabled = false;
      });
//			var val = _.find($scope.$parent.$parent.$parent.tabs, function(el, index){
//				var qModule = "Interview "+$scope.data[0].name;
//				if(el.title === qModule){
//					$scope.$parent.$parent.$parent.selectedIndex = index;
//					$rootScope.$broadcast('InterviewCtrl:update', scope.$modelValue.idNode);
//				}
//			});
      saveModuleWithoutReload();
    };

    $scope.enable = function(node) {
      var canEdit = true;
      if(node.nodeclass == 'M') {
        canEdit = false;
      } else if(node.nodeclass == 'F') {
        canEdit = false;
      }
      if(node.type == 'Q_linkedajsm') {
        canEdit = false;
      } else if(node.type == 'Q_linkedmodule') {
        canEdit = false;
      }
      if(canEdit) {
        recordAction($scope.data);
        $scope.safeApply(function() {
          node.editEnabled = true;
          if(node.name == 'New Question') {
            node.name = "";
          } else if(node.name == 'New Possible Answer') {
            node.name = "";
          }
        });
      }
    };
    $scope.safeApply = function(fn) {
      var phase = this.$root.$$phase;
      if(phase == '$apply' || phase == '$digest') {
        if(fn && (typeof (fn) === 'function')) {
          fn();
        }
      } else {
        this.$apply(fn);
      }
    };
    $scope.showRulesMenu = function(scope) {
      return $scope.rulesMenuOptions;
    };
    $scope.showRulesTopMenu = function(scope) {
      return $scope.rulesTopMenuOptions;
    };
    $scope.showMenu = function(node) {
      if(node.nodeclass == 'M') {
        var menu = $scope.moduleMenuOptions;
        if(node.type != 'M_IntroModule') {
          _.remove(menu, {
            0: 'Run Interview'
          });
        }
        return menu;


      } else if(node.nodeclass == 'F') {
        var menu = $scope.moduleMenuOptions;
        _.remove(menu, {
          0: 'Run Interview'
        });
        return menu;
      } else if(node.nodeclass == 'Q') {
        $scope.selectedNode = node;
        var menuOptions;
        if(node.type == 'Q_linkedmodule') {
          menuOptions = $scope.linkedModuleMenuOptions;
        } else if(node.type == 'Q_linkedajsm') {
          menuOptions = $scope.linkedAjsmMenuOptions;
        } else {
          menuOptions = $scope.questionMenuOptions;
        }
        return menuOptions;
      } else if(node.nodeclass == 'P') {
        return $scope.possibleAnswerMenuOptions;
      } else {
        return $scope.defauisReadOnlyEnabledltMenuOptions;
      }
    };


    var processAjsm = function(node) {
      var deferred = $q.defer();
      return QuestionsService.findQuestions(node.link, 'F').then(function(response) {
        var module = response.data[0];
        $rootScope.studyAjsmLinksCount = ($rootScope.studyAjsmLinksCount + 1);
        if(module) {
          var studyNodes = getStudyNodes(module.nodes, module);
          for(var i = 0; i < studyNodes.length; i++) {
            var node = studyNodes[i];
            //cascade up parents remove deleted mark
            removeDeletedMarkFromParent(node.parentId, module);
          }
          deferred.resolve(module);
        } else {
          deferred.reject('Could not find module');
        }
        return deferred.promise;
      });
    };


    function saveStudyModule(module) {
      var deferred = $q.defer();
      var nodes = module.nodes;
      var maxId = '';
      var parentId = module.idNode;
      var parentNodeNumber = module.number;
      var topNodeId = module.idNode;
      generateIdNodeCascade(nodes, maxId, parentId, parentNodeNumber, topNodeId);
      return QuestionsService.saveNode(module).then(function(response) {
        if(response.status === 200) {
          $log.info('Study Module ' + module.name + ' Saved');
          deferred.resolve(module);
        } else {
          deferred.reject('Could not save module');
        }
        return deferred.promise;
      });
    }

    function removeDeletedMarkFromParentCheckChildNode(parentNodeId, node, module) {
      var nodes = node.nodes;
      if(nodes.length > 0) {
        var i = 0;
        _.each(nodes, function(cnode) {
          if(cnode.idNode == parentNodeId) {
            cnode.deleted = 0;
            if(cnode.nodes) {
              if(cnode.nodes.length > 0) {
                _.each(cnode.nodes, function(ccnode) {
                  ccnode.deleted = 0;
                });
              }
            }
            if(cnode.parentId) {
              removeDeletedMarkFromParent(cnode.parentId, module);
            }
          } else {
            removeDeletedMarkFromParentCheckChildNode(parentNodeId, cnode, module);
          }
          i++;
        });
      }
    }

    function removeDeletedMarkFromParent(parentNodeId, module) {
      var nodes = module.nodes;
      if(nodes.length > 0) {
        var i = 0;
        _.each(nodes, function(cnode) {
          if(cnode.idNode == parentNodeId) {
            cnode.deleted = 0;
            if(cnode.nodes) {
              if(cnode.nodes.length > 0) {
                _.each(cnode.nodes, function(ccnode) {
                  ccnode.deleted = 0;
                });
              }
            }
            if(cnode.parentId) {
              removeDeletedMarkFromParent(cnode.parentId, module);
            }
          } else {
            removeDeletedMarkFromParentCheckChildNode(parentNodeId, cnode, module);
          }
          i++;
        });
      }
    }

    function getStudyNodes(nodes, module) {
      SystemPropertyService.getAll().then(function(response) {
        var sysprops = response.data;
        var ssagents = _.find(sysprops, function(sysprop) {
          return sysprop.type == 'studyagent';
        });
        var agents = [];
        _.each(ssagents, function(ssagent) {
          agents.push(ssagent.value)
        });
        var studyNodes = [];
        if(nodes.length > 0) {
          var i = 0;
          _.each(nodes, function(node) {
            var found = _.find(node.moduleRule, function(o) {
              var bFound = _.find(agents, function(agentId) {
                return o.idAgent == agentId;
              });
              if(bFound) {
                console.log("Found rule on " + node.idNode);
              }
              return bFound;
            });
            if(!found) {
              if(node.type.indexOf('frequency') == -1) {//not a frequency
                if(node.type.indexOf('linked') == -1) {//not a link
                  node.deleted = 1;
                } else {
                  studyNodes.push(node);
                }
              }
            } else {
              studyNodes.push(node);
            }
            if(node.nodes) {
              if(node.nodes.length > 0) {
                var sNodes = getStudyNodes(node.nodes, module);
                for(var i = 0; i < sNodes.length; i++) {
                  studyNodes.push(sNodes[i]);
                }
              }
            }
            i++;
          });
        }
        return studyNodes;
      });
    }

    function findAjsms(nodes) {
      if(nodes.length > 0) {
        var i = 0;
        _.each(nodes, function(node) {
          if(node.link) {
            if(node.type == 'Q_linkedajsm') {
              var moduleLinks = $rootScope.studyAjsmLinks;
              if(moduleLinks.length == 0) {
                moduleLinks.push(node);
                processAndSaveStudyAjsm(node);
              } else {
                var bFound = false;
                for(var i = 0; i < moduleLinks.length; i++) {
                  if(moduleLinks[i].link === node.link) {
                    bFound = true;
                    break;
                  }
                }
                if(!bFound) {
                  moduleLinks.push(node);
                  processAndSaveStudyAjsm(node);
                }
              }
            }
          } else {
            if(node.nodes) {
              if(node.nodes.length > 0) {
                findAjsms(node.nodes);
              }
            }
          }
          i++;
        });
      }
    }

    var processModuleAndChildAjsms = function(node) {
      var deferred = $q.defer();
      return QuestionsService.findQuestions(node.link, 'M').then(function(response) {
        var module = response.data[0];
        $rootScope.studyModuleLinksCount = ($rootScope.studyModuleLinksCount + 1);
        if(module) {
          findAjsms(module.nodes);
          var studyNodes = getStudyNodes(module.nodes, module);
          for(var i = 0; i < studyNodes.length; i++) {
            var node = studyNodes[i];
            //cascade up parents remove deleted mark
            removeDeletedMarkFromParent(node.parentId, module);
          }
          deferred.resolve(module);
        } else {
          deferred.reject('Could not find module');
        }
        return deferred.promise;
      });
    };

    function processAndSaveStudyAjsm(node) {
      var promise = processAjsm(node);
      promise.then(function(module) {
        console.log('Success on process Task Module: ' + module.name);
        var promise1 = saveStudyModule(module);
        promise1.then(function(module) {
          console.log('Success on save Task Module: ' + module.name);

        }, function(reason) {
          ngToast.create({
            className: 'danger',
            content: 'Failed: ' + reason,
            animation: 'slide'
          });
        }, function(update) {
          console.log('Got notification: ' + update);
        });
      }, function(reason) {
        ngToast.create({
          className: 'danger',
          content: 'Failed: ' + reason,
          animation: 'slide'
        });
      }, function(update) {
        console.log('Got notification: ' + update);
      });
    }

    function processAndSaveStudyModule(node) {
      var promise = processModuleAndChildAjsms(node);
      promise.then(function(module) {
        console.log('Success on process Module: ' + module.name);
        var promise1 = saveStudyModule(module);
        promise1.then(function(module) {
          console.log('Success on save Module: ' + module.name);

        }, function(reason) {
          ngToast.create({
            className: 'danger',
            content: 'Failed: ' + reason,
            animation: 'slide'
          });
        }, function(update) {
          console.log('Got notification: ' + update);
        });
      }, function(reason) {
        ngToast.create({
          className: 'danger',
          content: 'Failed: ' + reason,
          animation: 'slide'
        });
      }, function(update) {
        console.log('Got notification: ' + update);
      });
    }

    function processStudyModule(node) {
      var promise = processModuleAndChildAjsms(node);
      promise.then(function(module) {
        console.log('Success on process Module: ' + module.name);
        return module;
      }, function(reason) {
        ngToast.create({
          className: 'danger',
          content: 'Failed: ' + reason,
          animation: 'slide'
        });
      }, function(update) {
        console.log('Got notification: ' + update);
      });
    }

    function findModules(nodes) {
      if(nodes.length > 0) {
        var i = 0;
        _.each(nodes, function(node) {
          if(node.link) {
            if(node.type == 'Q_linkedmodule') {
              var moduleLinks = $rootScope.studyModuleLinks;
              if(moduleLinks.length == 0) {
                moduleLinks.push(node);
                processAndSaveStudyModule(node);
              } else {
                var bFound = false;
                for(var i = 0; i < moduleLinks.length; i++) {
                  if(moduleLinks[i].link === node.link) {
                    bFound = true;
                    break;
                  }
                }
                if(!bFound) {
                  moduleLinks.push(node);
                  processAndSaveStudyModule(node);
                }
              }
            }
          } else {
            if(node.nodes) {
              if(node.nodes.length > 0) {
                findModules(node.nodes);
              }
            }
          }
          i++;
        });
      }
    }

    $scope.moduleMenuOptions =
      [
        ['Show Rules', function($itemScope) {
          $scope.addRulesTab($itemScope);
        }
        ],
        ['Add Question', function($itemScope) {
          $scope.addingQuestion = true;
          $scope.newSubItem($itemScope);
        }
        ],
        ['Show/Hide Children', function($itemScope) {

          var collapseOrExpand = function(scope) {
            var i, subScope,
              nodes = scope.childNodes();
            for(i = 0; i < nodes.length; i++) {
              var node = nodes[i];
              if(node.childNodes().length > 0) {
                var collapsed = node.collapsed;
                !collapsed ? nodes[i].collapse() : nodes[i].expand();
                subScope = nodes[i].$childNodesScope;
                if(subScope) {
                  collapseOrExpand(subScope);
                }
              }
            }
          };
          collapseOrExpand($itemScope);
        }
        ], null, // Divider
        ['Run Interview', function($itemScope) {
          SystemPropertyService.getByName("activeIntro").then(function(response) {
            if(response.status == '200') {
              if(response.data == "") {
                if(confirm("This module is not an active intro module. Should we set this as active intro module?")) {
                  ModulesService.setActiveIntroModule($scope.data[0])
                    .then(function(response) {
                      if(response.status == '200') {
                        var newScope = $itemScope.$new();
                        $mdDialog.show({
                          scope: newScope,
                          templateUrl: 'scripts/questions/view/interviewDialog.html',
                          parent: angular.element(document.body),
                          clickOutsideToClose: true
                        })
                          .then(function(answer) {
                          }, function() {
                          });
                      }
                    });
                }
              } else if(response.data.value) {
                if(response.data.value == moduleIdNode) {
                  var newScope = $itemScope.$new();
                  $mdDialog.show({
                    scope: newScope,
                    templateUrl: 'scripts/questions/view/interviewDialog.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: true
                  })
                    .then(function(answer) {
                    }, function() {
                    });
                } else {
                  if(confirm("This module is not an active intro module. Should we set this as active intro module?")) {
                    ModulesService.setActiveIntroModule($scope.data[0])
                      .then(function(response) {
                        if(response.status == '200') {
                          var newScope = $itemScope.$new();
                          $mdDialog.show({
                            scope: newScope,
                            templateUrl: 'scripts/questions/view/interviewDialog.html',
                            parent: angular.element(document.body),
                            clickOutsideToClose: true
                          })
                            .then(function(answer) {
                            }, function() {
                            });
                        }
                      });
                  }
                }
              }
            }
          });
        }
        ], null, // Divider
        ['Save Module As', function($itemScope) {
          var newScope = $itemScope.$new();
          newScope.name = $itemScope.$modelValue.name + "(Copy)";
          newScope.includeRules = true;
          newScope.includeLinks = true;

          newScope.vo = $itemScope.$modelValue;
          $mdDialog.show({
            scope: newScope,
            templateUrl: 'scripts/questions/view/saveAsDialog.html',
            parent: angular.element(document.body),
            clickOutsideToClose: true
          })
            .then(function(answer) {
              $scope.status = 'You said the information was "' + answer + '".';
            }, function() {
              $scope.status = 'You cancelled the dialog.';
            });
        }
        ], null, // Divider
        ['Export to Word', function($itemScope) {
          var newUrl = '';
          if(confirm('Should we filter it by study agent?Ok for Yes and Cancel to run without filter.')) {
            newUrl = 'web/rest/module/exportToWord?idNode=' + data[0].idNode + '&filterStudyAgent=true';
          } else {
            newUrl = 'web/rest/module/exportToWord?idNode=' + data[0].idNode + '&filterStudyAgent=false';
          }
          $http({
            method: 'GET',
            cache: false,
            url: newUrl,
            data: data[0].idNode,
            responseType: 'arraybuffer',
            headers: {
//					    'Authorization': "Bearer " + $rootScope.userInfo.access_token,
              'Access-Control-Allow-Origin': '*'
            }
          }).then(function(data) {
            var octetStreamMime = 'application/octet-stream';
            var success = false;

            // Get the headers
            var headers = data.headers();
            var blob = new Blob([data.data], {type: 'application/octet-stream'});
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = data.config.data + ".docx";
            link.click();
          });

//				  QuestionsService.exportToWord(data[0]).then(function(response){
//						if(response.status === 200){
//							var blob=new Blob([response.data], {type: 'application/msword'});
//						    var link=document.createElement('a');
//						    link.href=window.URL.createObjectURL(blob);
//						    link.download="test.doc";
//						    link.click();
//						}
//				  });
        }
        ], null,
        // [ 'Export to JSON', function($itemScope) {
        //   var activeModule = $scope.data[0];
        //   if(activeModule.type == 'M_IntroModule'){
        // 	  ngToast.create({
        //   		  className: 'warning',
        //   		  content: 'Cannot export an Intro module.'
        //   	 });
        //   }else{
        // 	//need to display pop up first
        // 	  var newScope = $itemScope.$new();
        // 	  newScope.name = $scope.data[0].name+"_"+$scope.data[0].idNode+".json";
        // 	  newScope.includeLinks = true;
        // 	  newScope.filterOnStudyAgents = false;
        // 	  $mdDialog.show({
        // 		  scope: newScope,
        // 	      templateUrl: 'scripts/questions/view/exportToJsonDialog.html',
        // 	      parent: angular.element(document.body),
        // 	      clickOutsideToClose:true
        // 	    })
        // 	    .then(function(answer) {
        // 	      $scope.status = 'You said the information was "' + answer + '".';
        // 	    }, function() {
        // 	      $scope.status = 'You cancelled the dialog.';
        // 	    });
        //   }
        // }
        // ],
        ['Export to PDF', function($itemScope) {

          var doc = new jsPDF('p', 'pt', 'a4');
          var options = {
            background: '#fff',
            pagesplit: true
          };
          // get node count
          totalNodeCount = 0;
          var count = $(".tree-node").length;
          if(count > 250) {
            // shrink nodes
            $(".tree-node").css({'font-size': '8px'});
            $(".tree-node").css({'height': '50px'});
            $(".badge").css({'font-size': '8px'});
          }
          var loc = $("#tree-root").get(0);
//				  		loc.style.height = '30000px';
          doc.addHTML(loc, 0, 0, options, function() {
            doc.save($itemScope.$modelValue.name + '.pdf');
            $(".tree-node").css({'font-size': '14px'});
            $(".tree-node").css({'height': 'auto'});
            $(".badge").css({'font-size': '12px'});
          });

//				  var pdf = new jsPDF('p', 'pt', 'b2');
//            	  pdf.canvas.height = 72 * 11;
//            	  pdf.canvas.width = 72 * 8.5;
//            	  var count = 0;
//            	  printPdf(data[0],pdf,count,$itemScope.$modelValue.name);
        }
        ],
        ['Set Active Intro Module', function($itemScope) {
          if(confirm('Are you sure you want to set  ' + $itemScope.$modelValue.name
            + ' as active intro module?')) {
            ModulesService.setActiveIntroModule($itemScope.$modelValue)
              .then(function(response) {
                ngToast.create({
                  className: 'success',
                  content: 'Active intro module set successfully.',
                  animation: 'slide'
                });
              });
          }
        }]
      ];
    if(auth.isLoggedIn() && auth.userHasPermission(['ROLE_ADMIN', 'ROLE_ADMIN'])) {
      $scope.moduleMenuOptions.unshift(['View Study Specific', function($itemScope) {
        previewStudySpecific();
      }
      ]);
    }
    $scope.questionMenuOptions =
      [['Add Possible Answer', function($itemScope) {
        $scope.newSubItem($itemScope);
      }
      ],
        ['Add Free Text Possible Answer', function($itemScope) {
          $itemScope.isFreeText = true;
          $scope.newSubItem($itemScope);
          $itemScope.isFreeText = false;
        }
        ],

        ['Multiple Choice (Toggle)', function($itemScope) {
          $scope.toggleMultipleChoice($itemScope);
        }
        ],
        ['Interview Display (Toggle)', function($itemScope) {
          $itemScope.$modelValue.description = $itemScope.$modelValue.description == 'display' ? '' : 'display';
          saveModuleWithoutReload();
        }
        ],

        ['Save as Task Module', function($itemScope) {

          $mdDialog.show({
            //scope: $scope,
            scope: $scope.$new(),
            /*locals: {
						  addFragmentTab: $scope.addFragmentTab
				         },*/
            templateUrl: 'scripts/questions/view/fragmentDialog.html',
            parent: angular.element(document.body),
            clickOutsideToClose: true
          })
            .then(function(answer) {
              $scope.status = 'You said the information was "' + answer + '".';
            }, function() {
              $scope.status = 'You cancelled the dialog.';
            });
        }
        ],
        ['Remove (Toggle)', function($itemScope) {
          $scope.deleteNode($itemScope);
        }
        ],
        ['Show/Hide Children', function($itemScope) {

          var toggleChildren = function(scope) {
            var i, subScope,
              nodes = scope.childNodes();
            for(i = 0; i < nodes.length; i++) {
              subScope = nodes[i].$childNodesScope;
              if(subScope) {
                var collapsed = !subScope.collapsed;
                for(i = 0; i < nodes.length; i++) {
                  collapsed ? nodes[i].collapse() : nodes[i].expand();
                }
              }
            }
          };
          toggleChildren($itemScope);
        }
        ]
      ];
    $scope.linkedAjsmMenuOptions =
      [
        ['Remove (Toggle)', function($itemScope) {
          $scope.deleteNode($itemScope);
        }
        ],
        ['Open as Task Module', function($itemScope) {
          FragmentsService.checkExists($itemScope.node.link).then(function(response) {
            if(response) {
              var node = angular.copy($itemScope.node);
              node.idNode = node.link;
              node.type = 'F_ajsm';
              node.classtype = 'F';
              $scope.addFragmentTab(node);
            } else {
              $itemScope.node.warning = 'warning';
            }
          });
        }
        ]
      ];

    function clone(obj) {
      if(null == obj || "object" != typeof obj) return obj;
      var copy = obj.constructor();
      for(var attr in obj) {
        if(obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
      }
      return copy;
    }


    $scope.linkedModuleMenuOptions =
      [
        ['Remove (Toggle)', function($itemScope) {
          $scope.deleteNode($itemScope);
        }
        ],
        ['Open as Module', function($itemScope) {
          var node = angular.copy($itemScope.node);
          node.idNode = node.link;
          node.type = 'M_Module';
          node.classtype = 'M';
          $scope.addModuleTab(node);
        }
        ]
      ];

    function addPopoverInfo(x, idRule) {
      if(angular.isUndefined(x[0].info)) {
        x[0].info = [];
      }
      x[0].info["Node" + x[0].idNode + idRule] = {
        idNode: x[0].idNode,
        idRule: idRule,
        nodeclass: x[0].nodeclass,
        nodePopover: {
          isOpen: false
        },
        nodePopoverInProgress: false
      };
    }

    function populateChildNodesOfFragment(data, fragments) {
      return FragmentsService.findFragment(data.fragmentId).then(function(response) {
        if(response.length > 0) {
          fragments.push(response[0]);
        }
      });
    }

    function populateChildNodesOfModules(data, modules) {
      return ModulesService.getWithFragments(data.moduleLinkId).then(function(response) {
        if(response.status == '200' && response.data.length > 0) {
          if(response.data.length > 0) {
            modules.push(response.data[0]);
          }
        }
      });
    }

    function getUpdatedModuleRule(model, deffered) {
      ModuleRuleService.getModuleRule(model.idNode).then(function(data) {
        if(data.data) {
          deffered.resolve(data.data);
        }
      });
      return deffered.promise;
    }

    function getUpdatedModuleRulesForWholeModuleForThisAgent(agentId, deffered) {
      var moduleId = $scope.data[0].idNode;
      ModuleRuleService.getModuleRulesFromModuleForAgent(moduleId, agentId).then(function(response) {
        if(response.data) {
          deffered.resolve(response.data);
        }
      });
      return deffered.promise;
    }


    $scope.rulesMenuOptions =
      [
        ['Show Rules', function($itemScope, $event, model) {
          var deffered = $q.defer();
          var promise = getUpdatedModuleRule(model, deffered);
          promise.then(function(data) {
            var mRules = _.filter(data, function(r) {
              return $itemScope.$parent.obj.idAgent === r.idAgent;
            });
            if(mRules.length > 0) {
              for(var i = 0; i < mRules.length; i++) {
                var scope = $itemScope.$new();
                scope.model = model;
                scope.rule = mRules[i].rule;
                scope.agentName = mRules[i].agentName;
                var x = scope.rule.conditions;
                x.idRule = scope.rule.idRule;
                addPopoverInfo(x, scope.rule.idRule);
                showRuleDialog($event.currentTarget.parentElement, scope, $compile);
                $scope.activeRule = scope.rule;
              }
            }
          });
        }
        ],
        ['Add Rule', function($itemScope, $event, model) {
          var conditions = [];
          conditions.push(model);
          $itemScope.model = model;
          var rule = {agentId: $itemScope.$parent.obj.idAgent, conditions: conditions, level: 'noExposure'};
          RulesService.create(rule).then(function(response) {
            if(response.status === 200) {
              if(response.data.idRule) {
                ModuleRuleService.getModuleRule(model.idNode).then(function(response) {

                  if(response.status === 200) {
                    var result = response.data[response.data.length - 1];
                    $itemScope.rule = result.rule;
                    $itemScope.agentName = result.agentName;
                    var x = $itemScope.rule.conditions;
                    addPopoverInfo(x, $itemScope.rule.idRule);
                    newNote($event.currentTarget.parentElement, $itemScope, $compile);
                    $scope.activeRule = result.rule;
                    if($itemScope.rules == null) {
                      $itemScope.rules = [];
                    }
                    if(angular.isUndefined(model.moduleRule)) {
                      model.moduleRule = [];
                    }

                    _.merge(model.moduleRule, response.data);
                    if(!model.moduleRule.$$phase) {
                      try {
                        model.moduleRule.$digest();
                      } catch(e) {
                      }
                    }
                    if(!$scope.data[0].moduleRule) {
                      $scope.data[0].moduleRule = [];
                    }
                    $scope.data[0].moduleRule.push(result);
                    initAgentData();
                  }
                });
              } else {
                ngToast.create({
                  className: 'danger',
                  content: 'Try reload',
                  animation: 'slide'
                });
              }
            }
          });

        }
        ]
      ];
    $scope.rulesTopMenuOptions =
      [
        ['Show Rules', function($itemScope, $event, model) {
          var agentId = $event.target.id;
          var deffered = $q.defer();
          var promise = getUpdatedModuleRulesForWholeModuleForThisAgent(agentId, deffered);
          promise.then(function(data) {
            var mRules = data;
            if(mRules.length > 0) {
              $scope.scrollToTop();
              var uniqueRules = [];
              for(var i = 0; i < mRules.length; i++) {
                var theRule = mRules[i].rule;
                var bFound = false;
                for(var j = 0; j < uniqueRules.length; j++) {
                  uRule = uniqueRules[j];
                  if(uRule.idRule == theRule.idRule) {
                    bFound = true;
                  }
                }
                if(!bFound) {
                  uniqueRules.push(theRule);
                  var scope = $itemScope.$new();
                  scope.model = model;
                  scope.rule = theRule;
                  scope.agentName = mRules[i].agentName;
                  var x = scope.rule.conditions;
                  x.idRule = scope.rule.idRule;
                  addPopoverInfo(x, scope.rule.idRule);
                  var targetDiv = angular.element("#allModuleRulesOfAgent");
                  newNote(targetDiv, scope, $compile);
                  $scope.activeRule = scope.rule;
                }
              }
            } else {
              var msg = 'No rules to show';
              ngToast.create({
                className: 'warning',
                content: msg,
                dismissOnTimeout: false,
                dismissButton: true
              });
            }
          });
        }
        ]
      ];


		    $scope.possibleAnswerMenuOptions = [
				[ 'Add Question', function($itemScope) {
					$scope.newSubItem($itemScope);
				} ],
				[ 'Remove (Toggle)', function($itemScope) {
					$scope.deleteNode($itemScope);
				} ],
				[
						'Show/Hide Children',
						function($itemScope) {

							var toggleChildren = function(nodes) {
								// var nodes = $itemScope.childNodes();
								for (var i = 0; i < nodes.length; i++) {
									var subScope = nodes[i].$childNodesScope;
									if (subScope) {
										var collapsed = !subScope.collapsed;
										for (i = 0; i < nodes.length; i++) {
											collapsed ? nodes[i].collapse()
													: nodes[i].expand();
										}
									}
								}
							};
							toggleChildren($itemScope.childNodes());
						} ] ];
		
	
	
	$scope.addFragmentAsChild = function(selected,destination){
		var node = {

				name : selected.name,
				description : selected.description,
				topNodeId : selected.idNode,
				type : 'Q_linkedajsm',
				nodeclass : selected.nodeclass,
				link : selected.idNode,
				parentId : destination.$modelValue.idNode,
				nodes : []
		};
		destination.$modelValue.nodes.push(node);
        reorderSequence($scope.data);
        saveModuleWithoutReload();
        updateRuleDialogIfExist(node);
		$scope.cancel();
	}	    

	var displayModuleSelectDialog = function(type,$itemScope){
		$scope.moduleDialog = {
				source:$itemScope,
				modules:[],
				loadFragmentModule:true,
				type:type==0?'Job Module':'Task Module'
		};
		
		if (type == 0) {
			 FragmentsService.get().then(function(data) {
			        var object = {};
			        var ajsms = [];
			        for(var i = 0; i < data.length; i++) {
			          var node = data[i];
			          //node.idNode = "";
			          node.nodeclass = "Q";
			          if(node.type == 'F_ajsm') {
			            node.type = "Q_linkedajsm";
			            ajsms.push(node);
			          }
			        }
			        $scope.moduleDialog.modules = ajsms;
			        $scope.moduleDialog.loadFragmentModule = false;
			});
		} else {
			ModulesService.getActiveModules().then(function(data) {
				for (var i = 0; i < data.length; i++) {
					var node = data[i];
					node.type = "Q_linkedmodule";
					node.nodeclass = "Q";
				}
				$scope.moduleDialog.modules = data;
				$scope.moduleDialog.loadFragmentModule = false;
			});
		}
		
		$mdDialog.show({
	          scope: $scope,
	          preserveScope: true,
	          templateUrl: 'scripts/questions/partials/moduleSelectDialog.html',
	          clickOutsideToClose: false
	    });
		
	}
		    
	var addMenuToPossibleAnswer = function(){
		if($scope.data[0].type == 'M_IntroModule') {
			$scope.possibleAnswerMenuOptions.push(
					[ 'Add Job Module', function($itemScope) {
						displayModuleSelectDialog(0,$itemScope);
					} ]);
		}else{
			$scope.possibleAnswerMenuOptions.push(
				[ 'Add Task Module', function($itemScope) {
					displayModuleSelectDialog(1,$itemScope);
				} ]);
		}
	}	    
	addMenuToPossibleAnswer();
	
	
		    
    $scope.defaultMenuOptions =
      [
        ['Remove (Toggle)', function($itemScope) {
          $scope.deleteNode($itemScope);
        }
        ]
      ];

    function saveModuleAndReload(locationId) {
      QuestionsService.getMaxId().then(function(response) {
        if(response.status === 200) {
          var nodes = $scope.data[0].nodes;
          var maxId = response.data;
          var parentId = $scope.data[0].idNode;
          var parentNodeNumber = $scope.data[0].number;
          var topNodeId = $scope.data[0].idNode;
          generateIdNodeCascade(nodes, maxId, parentId, parentNodeNumber, topNodeId);
          QuestionsService.saveNode($scope.data[0]).then(function(response) {
            if(response.status === 200) {
              $log.info('Save was Successful Now Reloading!');
              QuestionsService.findQuestions($scope.data[0].idNode, $scope.data[0].nodeclass).then(function(data) {

                $scope.data = data.data;
                if(locationId) {
                  //$scope.scrollTo(locationId);
                }
              });
            } else {
              $log.error('ERROR on Save!');
              throw response;
            }
          });
        } else {
          $log.error('ERROR on Save!');
          throw response;
        }
      });
    }

    function saveModuleWithoutReload(locationId, deffered) {
      QuestionsService.getMaxId().then(function(response) {
        if(response.status === 200) {
          var node_data = $scope.data[0];
          var nodes = node_data.nodes;
          var maxId = response.data;
          var parentId = node_data.idNode;
          var parentNodeNumber = node_data.number;
          var topNodeId = node_data.idNode;
          generateIdNodeCascade(nodes, maxId, parentId, parentNodeNumber, topNodeId);
          QuestionsService.saveNode(node_data).then(function(response) {
            if(response.status === 200) {
              $log.info('Save was Successful! Not Reloading ' + node_data.name);
              if(deffered) {
                deffered.resolve();
              }
              if($scope.addingQuestion) {
                $scope.scrollTo(locationId);
                $scope.highlight(locationId);
                $scope.addingQuestion = false;
              }
            } else {
              $log.error('ERROR on Save!' + response.status.message);
              if(deffered) {
                deffered.reject();
                throw response;
              }
            }
          });
        } else {
          $log.error('ERROR on Get max ID!' + response.status.message);
          if(deffered) {
            deffered.reject();
            throw response;
          }
        }
      });
    }

    function saveModuleWithReloadDeff(locationId, deffered) {
      QuestionsService.getMaxId().then(function(response) {
        if(response.status === 200) {
          var nodes = $scope.data[0].nodes;
          var maxId = response.data;
          var parentId = $scope.data[0].idNode;
          var parentNodeNumber = $scope.data[0].number;
          var topNodeId = $scope.data[0].idNode;
          generateIdNodeCascade(nodes, maxId, parentId, parentNodeNumber, topNodeId);
          QuestionsService.saveNode($scope.data[0]).then(function(response) {
            if(response.status === 200) {
              $log.info('Save was Successful Now Reloading!');
//							$state.reload();
//							$state.go($state.current, {row: $scope.data[0].idNode}, {reload: true});
              $scope.reloadTab($scope.data[0]);
            } else {
              $log.error('ERROR on Save!' + response.status.message);
              if(deffered) {
                deffered.reject();
                throw response;
              }
            }
          });
        } else {
          $log.error('ERROR on Get max ID!' + response.status.message);
          if(deffered) {
            deffered.reject();
            throw response;
          }
        }
      });
    }


    function saveAsFragment(data, mydata) {
      QuestionsService.getMaxId().then(function(response) {
        if(response.status === 200) {
          var maxId = response.data + 1;
          destNode = {
            idNode: maxId,
            name: mydata.name,
            type: mydata.group,
            nodeclass: "F",
            nodes: []
          };
          var childNodes = angular.copy(data.nodes);
          destNode.nodes.unshift({
            name: data.name,
            description: data.description,
            type: data.type,
            nodeclass: data.nodeclass,
            nodes: childNodes
          });
          cascadeTemplateNullIds(destNode.nodes);
          var nodes = destNode.nodes;
          var parentId = destNode.idNode;
          var parentNodeNumber = 0;
          var topNodeId = destNode.idNode;
          //generateIdNodeCascadeFragment(nodes,maxId,parentId);
          generateIdNodeCascade(nodes, maxId, parentId, parentNodeNumber, topNodeId);


          var deffered = $q.defer();
          FragmentsService.createFragment(destNode).then(function(response) {
            if(response.status === 200) {
              $log.info("Fragment saved");
              FragmentsService.getByType('F_template').then(function(template) {
                for(var i = 0; i < template.length; i++) {
                  var node = template[i];
                  node.nodeclass = "Q";
                }
                if($scope.templateData != null) {
                  _.merge($scope.templateData, template);
                }
                deffered.resolve();
              });
              FragmentsService.getByType('F_ajsm').then(function(data) {
                for(var i = 0; i < data.length; i++) {
                  var node = data[i];
                  node.type = "Q_linkedajsm";
                  node.nodeclass = "Q";
                }
                if($scope.aJSMData != null) {
                  _.merge($scope.aJSMData, data);
                }
                deffered.resolve();
              });
              FragmentsService.getByType('F_frequency').then(function(data) {
                for(var i = 0; i < data.length; i++) {
                  var node = data[i];
                  node.nodeclass = "Q";
                }
                if($scope.frequencyData != null) {
                  _.merge($scope.frequencyData, data);
                }
                deffered.resolve();
              });

            } else {
              deffered.reject();
              $log.error(response.status);
              throw response.status.message;
            }
          });
          deffered.promise.then(function() {
            $mdDialog.hide();
          });
        }
      });
    }

    var increment = 0;

    function generateIdNodeCascadeFragment(arrayInp, maxId, parentId) {
      increment = maxId;
      if(arrayInp.length > 0) {
        var i = 0;
        _.each(arrayInp, function(node) {

          node.sequence = i;
          if(!node.idNode) {
            increment = (increment + 1);
            node.idNode = increment;

            if(parentId) {
              node.parentId = parentId;
            }
          }
          if(node.nodes) {
            if(node.nodes.length > 0) {
              generateIdNodeCascadeFragment(node.nodes, increment, node.idNode);
            }
          }
          i++;
        });

      }
    }


    var maxIdIncrement = 0;

    function generateIdNodeCascade(arrayInp, maxId, parentId, parentNodeNumber, topNodeId) {
      maxIdIncrement = maxId;

      if(arrayInp.length > 0) {
        var i = 0;
        _.each(arrayInp, function(node) {

          node.parentId = parentId;
          node.sequence = i;
          node.topNodeId = topNodeId;
          if(node.nodeclass == 'Q') {
            node.number = parentNodeNumber + (i + 1);
          } else if(node.nodeclass == 'P') {
            if(!isNaN(parentNodeNumber)) {
              if(i > 26) {
                var character = String.fromCharCode('A'.charCodeAt() + (i - 27));
                node.number = parentNodeNumber + 'Z' + $scope.getNextKey(character);
              } else {
                var character = String.fromCharCode('A'.charCodeAt() + (i - 1));
                node.number = parentNodeNumber + $scope.getNextKey(character);
              }

            } else {
              var length = parentNodeNumber.length;
              var lastCharacter = parentNodeNumber.substring(length - 1);
              if(!isNaN(lastCharacter)) {
                if(i > 26) {
                  var character = String.fromCharCode('A'.charCodeAt() + (i - 27));
                  node.number = parentNodeNumber + 'Z' + $scope.getNextKey(character);
                } else {
                  var character = String.fromCharCode('A'.charCodeAt() + (i - 1));
                  node.number = parentNodeNumber + $scope.getNextKey(character);
                }

              }
            }

          }
          if(!node.idNode) {
            maxIdIncrement = (maxIdIncrement + 1);
            node.idNode = maxIdIncrement;
          }
          if(node.nodes) {
            if(node.nodes.length > 0) {
              generateIdNodeCascade(node.nodes, maxIdIncrement, node.idNode, node.number, topNodeId);
            }
          }
          i++;
        });
      }
    }

    $scope.getNextKey = function(key) {
      if(/^Z+$/.test(key)) {
        // If all z's, replace all with a's
        key = key + 'A';
      } else {
        // (take till last char) append with (increment last char)
        key = key.slice(0, -1) + String.fromCharCode(key.slice(-1).charCodeAt() + 1);
      }
      return key;
    };
    $scope.saveModule = function() {
      saveModuleAndReload();
    };

    $scope.saveAsFragment = function(data, mydata) {
      saveAsFragment(data, mydata);
    };

    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    function recordAction(data) {
      $window.beforeNode = angular.copy(data);
      $scope.undoEnable = true;
    }

    $scope.undo = function($itemScope) {
      $log.info("Undo is being processed ........");
      QuestionsService.getMaxId().then(function(response) {
        if(response.status === 200) {
          var nodes = $window.beforeNode[0].nodes;
          var maxId = response.data;
          var parentId = $window.beforeNode[0].idNode;
          var parentNodeNumber = $window.beforeNode[0].number;
          var topNodeId = $window.beforeNode[0].idNode;
          generateIdNodeCascade(nodes, maxId, parentId, parentNodeNumber, topNodeId);
          QuestionsService.saveNode($window.beforeNode[0]).then(function(response) {
            if(response.status === 200) {
              ngToast.create({
                className: 'success',
                content: 'Undo successful ' + $window.beforeNode[0].name,
                dismissOnTimeout: true,
                dismissButton: true
              });
              $itemScope.$modelValue.nodes = $window.beforeNode[0].nodes;
            } else {
              ngToast.create({
                className: 'danger',
                content: 'Undo failed ' + response.status.message,
                dismissOnTimeout: true,
                dismissButton: true
              });
            }
          });
        } else {
          ngToast.create({
            className: 'danger',
            content: 'Undo failed ERROR on Get max ID!' + response.status.message,
            dismissOnTimeout: true,
            dismissButton: true
          });
        }
      });
      $scope.undoEnable = false;
    };

    $scope.isClonable = false;
    $scope.copy = function(event, $treeScope) {
      if(event.ctrlKey) {
        $scope.isClonable = true;
        $treeScope.cloneEnabled = true;
        safeDigest($treeScope);
      }
    };

    $scope.scrollTo = function(target) {
      //var scrollPane = $("body");
      if((target == "") || (target == undefined))
        return;
      var scrollTarget = $('#' + target);
      var scrollY = scrollTarget.offset().top - 150;
      //scrollPane.animate({scrollTop : scrollY }, 1000, 'swing');

      $('html, body').animate({
        scrollTop: scrollY
      }, 2000, 'swing');
    };

    $scope.highlightNode = function(idNode) {
      var elementId = 'node-' + idNode;
      $scope.scrollTo(elementId);
      $('#' + elementId).toggleClass('highlight');
    };

    $scope.highlight = function(idNode) {
      $('#' + idNode).toggleClass('highlight');
    };

    function getObject(array, idNode) {
      var object = _.find(array, _.matchesProperty('idNode', idNode));
      if(object != null && !angular.isUndefined(object)) {
        return object;
      }
      var obj;
      _.forEach(array, function(v, k) {
        if(v.nodes) {
          obj = getObject(v.nodes, idNode);
          if(obj != null) {
            return false;
          }
        }
      });
      return obj;
    }

    $rootScope.tabsLoading = false;

    $scope.closeRuleDialog = function(elem, $event) {
      $($event.target).closest('.note').remove();
      $scope.activeRuleDialog = '';
      $scope.activeRuleCell = '';
      safeDigest($scope.activeRuleDialog);
      safeDigest($scope.activeRuleCell);
    };

    $scope.setActiveRule = function(rule, el) {
      var firstRuleNodeId = el.model.idNode;
      if(!firstRuleNodeId) {
        el.model.idNode = rule.conditions[0].idNode;
        firstRuleNodeId = rule.conditions[0].idNode;
      }
      $scope.activeRuleDialog = firstRuleNodeId + '-' + rule.agentId + '-' + rule.idRule;
      $scope.activeRuleCell = el.model.idNode + rule.agentId;
      $scope.activeRuleEl = el;
      $scope.activeRule = rule;
      if(!$scope.activeRuleDialog.$$phase) {
        try {
          $scope.activeRuleDialog.$digest();
        } catch(e) {
        }
      }
    };
    $scope.addToActiveRule = function(node, rules) {

      var rule = $scope.activeRule;
      var bAlreadyInRule = false;
      var nodeNumbers = [];
      for(var i = 0; i < rule.conditions.length; i++) {
        var iCondition = rule.conditions[i];
        if(iCondition.idNode == node.idNode) {
          bAlreadyInRule = true;
          break;
        }
        if(iCondition.number) {
          nodeNumbers.push(iCondition.number);
        }
      }
      for(var i = 0; i < nodeNumbers.length; i++) {
        if(_.startsWith(nodeNumbers[i], node.number)) {
          bAlreadyInRule = true;
          break;
        }
      }
      if(bAlreadyInRule) {
        ngToast.create({
          className: 'warning',
          content: 'Child node already has this rule.',
          dismissOnTimeout: true,
          dismissButton: true
        });
        return;
      }

      if(!bAlreadyInRule) {
        rule.conditions.push(node);
        if(rules.rules == null) {
          rules.rules = [];
        }
        rules.rules.push({
          idNode: node.idNode
        });
        RulesService.save(rule).then(function(response) {
          if(response.status === 200) {
            $log.info('Rule Save was Successful!' + rule);
            ModuleRuleService.getModuleRule(node.idNode).then(function(response) {
              if(response.status === 200) {
                var result = response.data[response.data.length - 1];
                if(angular.isUndefined(node.moduleRule)) {
                  node.moduleRule = [];
                }
                _.merge(node.moduleRule, response.data);
                if(!node.moduleRule.$$phase) {
                  try {
                    node.moduleRule.$digest();
                  } catch(e) {
                  }
                }
//    			    			resize();
              }
            });
          }
        });
      }
    };

    function resize() {
      //get element

      var height = document.getElementById($scope.activeRuleDialog).style.height;

      //transform element from string to integer

      height = height.replace("px", "");
      height = +height;

      //change height

      var n = height;
      height = n + 300 + 'px';
      document.getElementById($scope.activeRuleDialog).style.height = height;
    }

    $scope.saveRule = function(rule) {
      RulesService.save(rule).then(function(response) {
        if(response.status === 200) {
          $log.info('Rule Save was Successful!' + rule);
          _.each(rule.conditions, function(v, k) {
            ModuleRuleService.getModuleRule(v.idNode).then(function(response) {
              if(response.status === 200) {
                var node = getObject($scope.data[0].nodes, v.idNode);
                if(!angular.isUndefined(node)) {
                  if(angular.isUndefined(node.moduleRule)) {
                    node.moduleRule = [];
                  }
                  _.merge(node.moduleRule, response.data);
                  safeDigest(node.moduleRule);
                }
              }
            });
          });
        }
      });

    };

    var safeDigest = function(obj) {
      if(!obj.$$phase) {
        try {
          obj.$digest();
        } catch(e) {
        }
      }
    };

    $scope.updateRule = function(rule, model) {
      RulesService.update(rule).then(function(response) {
        if(response.status === 200) {
          $log.info('Rule Save was Successful!' + rule);
          ModuleRuleService.getModuleRule(model.idNode).then(function(response) {
            if(response.status === 200) {
              var result = response.data[response.data.length - 1];
              if(angular.isUndefined(model.moduleRule)) {
                model.moduleRule = [];
              }
              _.merge(model.moduleRule, response.data);
              if(!model.moduleRule.$$phase) {
                try {
                  model.moduleRule.$digest();
                } catch(e) {
                }
              }
            }
          });
        }
      });

    };
    $scope.removeNodeFromRule = function(node) {
      var rule = $scope.activeRule;

      for(var i = 0; i < rule.conditions.length; i++) {
        var iCondition = rule.conditions[i];
        if(iCondition.idNode == node.idNode) {
          rule.conditions.splice(i, 1);
        }
      }
      RulesService.save(rule).then(function(response) {
        if(response.status === 200) {
          $log.info('Rule Save was Successful!' + rule);
          ModuleRuleService.getModuleRule(node.idNode).then(function(response) {
            if(response.status === 200) {
              var result = response.data[response.data.length - 1];
              if(angular.isUndefined(node.moduleRule)) {
                node.moduleRule = [];
              }
              _.each(node.moduleRule, function(mr) {
                var isExist = false;
                _.each(response.data, function(dt) {
                  if(mr.rule) {
                    if(mr.rule.idRule === dt.rule.idRule) {
                      isExist = true;
                    }
                  }
                });
                if(isExist == false) {
                  var index = node.moduleRule.indexOf(mr);
                  if(index > -1) {
                    node.moduleRule.splice(index, 1);
                  }
                }
              });
              if(!node.moduleRule.$$phase) {
                try {
                  model.moduleRule.$digest();
                } catch(e) {
                }
              }
            }
          });
          initAgentData();
        }
      });
    };
    $scope.newModName = null;
    $scope.includeRuleInMod = null;
    $scope.saveAsModule = function(vo, name, includeRules, includeLinks) {
      var copyVO = {
        vo: angular.copy(vo),
        name: name,
        includeRules: includeRules,
        includeLinks: includeLinks
      };
      if('F' == vo.nodeclass) {
        FragmentsService.copyModule(copyVO).then(function(data) {
          var row = {};
          row.name = name;
          row.idNode = data.data;
          $mdDialog.hide();
          $scope.addFragmentTab(row);

        });
      }
      if('M' == vo.nodeclass) {
        ModulesService.copyModule(copyVO).then(function(data) {
          var row = {};
          row.name = name;
          row.idNode = data.data;
          $mdDialog.hide();
          $scope.addModuleTab(row);

        });
      }
    };

    function exportJsonForIntroModule(copyData, name, includeLinks) {
      // get modules from modules view
      var modules = [];
      var promises = [];
      ModulesService.getModuleIntroModuleByModuleId(copyData[0].idNode).then(function(response) {
        if(response.status == '200') {
          // loop each module get details for each
          _.each(response.data, function(data) {
            if(includeLinks) {
              promises.push(populateChildNodesOfModules(data, modules));
            }
          });
          $q.all(promises).then(function() {
            console.log('finish creating the JSON.. exporting in progress.');
            copyData[0].modules = modules;
            var blob = new Blob([JSON.stringify(copyData)], {
              type: "application/json;charset=" + "utf-8" + ";"
            });

            if(window.navigator.msSaveOrOpenBlob) {
              navigator.msSaveBlob(blob, name);
            } else {
              var downloadContainer = angular.element('<div data-tap-disabled="true"><a></a></div>');
              var downloadLink = angular.element(downloadContainer.children()[0]);
              downloadLink.attr('href', window.URL.createObjectURL(blob));
              downloadLink.attr('download', name);
              downloadLink.attr('target', '_blank');

              $document.find('body').append(downloadContainer);
              $timeout(function() {
                downloadLink[0].click();
                downloadLink.remove();
              }, null);
            }
          });
          $mdDialog.hide();
        }
      });
    }

    function exportToJsonFilterStudyAgents(copyData, name) {
      var fragments = [];
      var promises = [];
      //get filtered module
      ModulesService.getModuleFilterStudyAgent(copyData[0].idNode).then(function(response) {
        if(response.status == '200') {
          if(response.data[0] == null) {
            ngToast.create({
              className: 'danger',
              content: 'Warning: No study agent exist.',
              animation: 'slide'
            });
            return;
          }

          var studyAgentData = response.data;
          //get list of ajsm
          ModulesService.getModuleFragmentByModuleId(studyAgentData[0].idNode).then(function(response) {
            if(response.status == '200') {
              // loop each fragment get details for each, filter study agents
              _.each(response.data, function(data) {
                promises.push(populateChildNodesOfFragmentFilterStudyAgents(data, fragments));
              });
              $q.all(promises).then(function() {
                console.log('finish creating the JSON.. exporting in progress.');
                studyAgentData[0].fragments = fragments;
                var blob = new Blob([JSON.stringify(studyAgentData)], {
                  type: "application/json;charset=" + "utf-8" + ";"
                });

                if(window.navigator.msSaveOrOpenBlob) {
                  navigator.msSaveBlob(blob, name);
                } else {

                  var downloadContainer = angular.element('<div data-tap-disabled="true"><a></a></div>');
                  var downloadLink = angular.element(downloadContainer.children()[0]);
                  downloadLink.attr('href', window.URL.createObjectURL(blob));
                  downloadLink.attr('download', name);
                  downloadLink.attr('target', '_blank');

                  $document.find('body').append(downloadContainer);
                  $timeout(function() {
                    downloadLink[0].click();
                    downloadLink.remove();
                  }, null);
                }
              });
            }
          });
          $mdDialog.hide();
        }
      });
    }

    function populateChildNodesOfFragmentFilterStudyAgents(data, fragments) {
      // todo filtering by study agent
      return FragmentsService.getFilterStudyAgents(data.fragmentId).then(function(response) {
        if(response[0] == null) {
          return;
        }
        if(response.length > 0) {
          fragments.push(response[0]);
        }
      });
    }

    function populateChildNodesOfFragmentFilterAgents(data, fragments, idAgent) {
      // todo filtering by study agent
      return FragmentsService.getFilterAgents(data.fragmentId, idAgent).then(function(response) {
        if(response[0] == null) {
          return;
        }
        if(response.length > 0) {
          fragments.push(response[0]);
        }
      });
    }

    function populateChildNodesOfModuleFilterAgents(data, fragments, idAgent) {
      // todo filtering by study agent
      return ModulesService.getModuleFilterAgent(data.moduleLinkId, idAgent).then(function(response) {
        if(response[0] == null) {
          return;
        }
        if(response.length > 0) {
          fragments.push(response[0]);
        }
      });
    }

    function exportJsonForModule(copyData, name, includeLinks) {
      var fragments = [];
      var promises = [];
      // get from module fragment view
      ModulesService.getModuleFragmentByModuleId(copyData[0].idNode).then(function(response) {
        if(response.status == '200') {
          // loop each fragment get details for each
          _.each(response.data, function(data) {
            if(includeLinks) {
              promises.push(populateChildNodesOfFragment(data, fragments));
            }
          });
          $q.all(promises).then(function() {
            console.log('finish creating the JSON.. exporting in progress.');
            copyData[0].fragments = fragments;
            var blob = new Blob([JSON.stringify(copyData)], {
              type: "application/json;charset=" + "utf-8" + ";"
            });

            if(window.navigator.msSaveOrOpenBlob) {
              navigator.msSaveBlob(blob, name);
            } else {

              var downloadContainer = angular.element('<div data-tap-disabled="true"><a></a></div>');
              var downloadLink = angular.element(downloadContainer.children()[0]);
              downloadLink.attr('href', window.URL.createObjectURL(blob));
              downloadLink.attr('download', name);
              downloadLink.attr('target', '_blank');

              $document.find('body').append(downloadContainer);
              $timeout(function() {
                downloadLink[0].click();
                downloadLink.remove();
              }, null);
            }
          });
          $mdDialog.hide();
        }
      });
    }

    function populateFragmentsForModule(module) {
      var fragments = [];
      var promises = [];
      // get from module fragment view
      ModulesService.getModuleFragmentByModuleId(module.idNode).then(function(response) {
        if(response.status == '200') {
          // loop each fragment get details for each
          _.each(response.data, function(data) {
            promises.push(populateChildNodesOfFragment(data, fragments));
          });
          $q.all(promises).then(function() {
            module.fragments = fragments;
          });
        }
      });
    }

    $scope.exportToJSON = function(name, includeLinks, filterOnStudyAgents) {
      var copyData = angular.copy($scope.data);
      var counter = 0;

//        	var ssmodule = copyData[0];
      if(filterOnStudyAgents) {
//        		ssmodule = processStudyModule(copyData[0]);
        exportToJsonFilterStudyAgents(copyData, name);
        return;
      }

      if(copyData[0].type == 'M_IntroModule') {
        exportJsonForIntroModule(copyData, name, includeLinks);
      } else {
        exportJsonForModule(copyData, name, includeLinks);
      }
    };

    $scope.deleteRule = function(rule, model, $event) {
      $scope.closeRuleDialog(model, $event);
      RulesService.remove(rule).then(function(response) {
        if(response.status === 200) {
          $log.info('Rule Save was Successful!' + rule);
          _.each(rule.conditions, function(v, k) {
            ModuleRuleService.getModuleRule(v.idNode).then(function(response) {
              if(response.status === 200) {
                var node = getObject($scope.data[0].nodes, v.idNode);
                if(!angular.isUndefined(node)) {
                  var moduleRuleIndex = _.findIndex($scope.data[0].moduleRule, function(item) {
                    return item.idNode === v.idNode && item.idAgent === rule.agentId
                  });
                  if(response.data.length < 1) {
                    node.moduleRule = [];
                    safeDigest(node.moduleRule);
                    if(moduleRuleIndex != -1) {
                      $scope.data[0].moduleRule.splice(moduleRuleIndex, 1);
                    }
                  } else {
                    node.moduleRule = response.data;
                    if(moduleRuleIndex != -1) {
                      $scope.data[0].moduleRule.splice(moduleRuleIndex, 1);
                    }
                    safeDigest(node.moduleRule);
                  }

                }

                initAgentData();
              }
            });
          });
        }
      });
    };

    $scope.reference = null;
    $scope.awesIdMaxSize = 7;
    $scope.awesIdSize = 0;
    $scope.filterAndValidate = function(event) {
      var elem = angular.element(document.querySelector('#awesid'));
      var counter = angular.element(document.querySelector('#awesidcounter'));
      var label = angular.element(document.querySelector('#awesidlabel'));
      if(event.which === 13 || event.which === 32) {
        if(awesIdIsValid(elem.val())) {
          self.add();
        } else {
          counter.append("Please enter a valid AWES ID");
        }
      } else {
        if(!awesIdIsValid(elem.val())) {
          elem.addClass("awesidwarning");
          label.addClass("awesidwarning");
          counter.addClass("awesidwarning");
        } else {
          elem.removeClass("awesidwarning");
          label.removeClass("awesidwarning");
          counter.removeClass("awesidwarning");
        }
      }
    };

    $scope.runInterview = function(reference) {
      if(awesIdIsValid($scope.searchAWESID)) {
        InterviewsService.checkReferenceNumberExists($scope.searchAWESID).then(function(data) {
          if(data.status == 200) {
            if(confirm("This AWES ID has already been used. Would you like to add a duplicate?")) {
              $scope.addInterviewTabInterviewers(-1, $scope.searchAWESID);
              $mdDialog.cancel();
            }
          } else if(data.status == 204) {
            $scope.addInterviewTabInterviewers(-1, $scope.searchAWESID);
            $mdDialog.cancel();
          } else {
            ngToast.create({
              className: 'danger',
              content: 'Error occured during checkReferenceNumberExists',
              animation: 'slide'
            });
          }
        })
      } else {
        ngToast.create({
          className: 'danger',
          content: 'You need to add a valid AWES ID before you can start.',
          animation: 'slide'
        });
      }
    };

    function awesIdIsValid(awesId) {
      $scope.searchAWESID = '';
      var retValue = false;
      $scope.awesIdSize = awesId.length;
      if($scope.awesIdSize == $scope.awesIdMaxSize) {
        if(_.startsWith(awesId, 'H')) {
          retValue = true;
          $scope.searchAWESID = awesId;
        }
      }
      return retValue;
    }

    function previewStudySpecific() {
      angular.element(document).find('.incl_expjson').removeClass('incl_expjson');
      var fragments = [];
      var promises = [];
      ModulesService.getModuleFilterStudyAgent(moduleIdNode).then(function(response) {
        if(response.status == '200') {
          if(response.data[0] == null) {
            ngToast.create({
              className: 'danger',
              content: "Warning: No study agent exist.",
              animation: 'slide'
            });
            return;
          }
          var studyAgentData = response.data;
          highlightStudySpecificNode(studyAgentData[0].nodes);
          ModulesService.getModuleFragmentByModuleId(studyAgentData[0].idNode).then(function(response) {
            if(response.status == '200') {
              // loop each fragment get details for each, filter study agents
              _.each(response.data, function(data) {
                promises.push(populateChildNodesOfFragmentFilterStudyAgents(data, fragments));
              });
              $q.all(promises).then(function() {
                if(studyAgentData[0].nodes.length < 1 && fragments.length < 1) {
                  ngToast.create({
                    className: 'danger',
                    content: "There is no study specific node for this tree, check the link task module.",
                    animation: 'slide'
                  });
                  return;
                }
                for(var i = 0; i < fragments.length; i++) {
                  higlightAjsms(fragments[i].idNode, $scope.data[0].nodes);
                }
              });
            }
          });
        }
      });
    }

    function higlightAjsms(fragmentIdNode, childNodes) {
      _.each(childNodes, function(item) {
        if(item.link == fragmentIdNode) {
          angular.element("#node-" + item.idNode).addClass("incl_expjson");
        }
        if(item.nodes.length > 0) {
          higlightAjsms(fragmentIdNode, item.nodes);
        }
      });
    }

    function highlightStudySpecificNode(childNodes) {
      for(var i = 0; i < childNodes.length; i++) {
        var node = childNodes[i];
        angular.element("#node-" + node.idNode).addClass("incl_expjson");
        if(node.nodes) {
          highlightStudySpecificNode(node.nodes);
        }
      }
    }

    $scope.currentToggledNodeWithAgent = undefined;
    $scope.toggleNodesWithAgent = function(agent, $event) {
      $event.stopPropagation();
      var resultRuleObj = _.find($scope.rulesObj, function(ro) {
        return ro.idAgent == agent.idAgent;
      });
      if($scope.currentToggledNodeWithAgent == agent.idAgent) {
        agent.active = false;
        angular.element(document).find('.nodeAgentEnabledBase').removeClass('nodeAgentEnabledBase');
        angular.element(document).find('.nodeAgentEnabledLinked').removeClass('nodeAgentEnabledLinked');
        angular.element(document).find('.nodeAgentEnabledDerived').removeClass('nodeAgentEnabledDerived');
        angular.element(document).find('.activeNodeAgentEnabled').removeClass('activeNodeAgentEnabled');
        $scope.currentToggledNodeWithAgent = undefined;
        if(agent.style == "agent-shown" && resultRuleObj) {
          $scope.safeApply(function() {
            $scope.rulesObj.splice(_.findIndex($scope.rulesObj, function(o) {
              return o.idAgent === agent.idAgent;
            }), 1)[0];
          });
          agent.style = "";
        }
        return;
      }
      angular.element(document).find('.nodeAgentEnabledBase').removeClass('nodeAgentEnabledBase');
      angular.element(document).find('.nodeAgentEnabledLinked').removeClass('nodeAgentEnabledLinked');
      angular.element(document).find('.nodeAgentEnabledDerived').removeClass('nodeAgentEnabledDerived');
      agent.active = true;
      if(!resultRuleObj) {
        $scope.rulesObj.push(agent);
        agent.style = "agent-shown";
      }
      highlightNodesWithAgent(agent.idAgent);
      $scope.currentToggledNodeWithAgent = agent.idAgent;
    };

//       function highlightNodesWithAgent(nodeAgentData){
//       		for(var i=0;i<nodeAgentData.length;i++){
//       			var node = nodeAgentData[i];
//    			angular.element("#node-"+node.idNode).addClass("nodeAgentEnabled");
//    			var nodeResult = findNodeById(node.idNode,$scope.data[0]);
//    			if(nodeResult){
//    				highlightParentNodeWithAgent(nodeResult.parentId);
//    			}
//			}
//       }

    function highlightNodesWithAgent(idAgent) {
      angular.element(document).find('.incl_expjson').removeClass('incl_expjson');
      var fragments = [];
      var modules = [];
      var promises = [];
      ModulesService.getModuleFilterAgent(moduleIdNode, idAgent).then(function(response) {
        if(response.status == '200') {
          if(response.data[0] == null) {
            ngToast.create({
              className: 'warning',
              content: "Warning: No agent rule exist for this module.",
              animation: 'slide'
            });
            return;
          }
          var agentData = response.data;
          highlightAgentSpecificNode(agentData[0].nodes);
//       			if($sessionStorage.activeIntro && $sessionStorage.activeIntro.value == moduleIdNode){
//    				//its an intro module
//       				ModulesService.getModuleIntroModuleByModuleId(agentData[0].idNode).then(function (response) {
//       					if(response.status == '200'){
//       						// loop each fragment get details for each, filter agents
//       						_.each(response.data,function(data){
//       							promises.push(populateChildNodesOfModuleFilterAgents(data,modules,idAgent));
//       						});
//       						$q.all(promises).then(function () {
//       							if(agentData[0].nodes.length < 1 && modules.length < 1){
//       								alert("There is no study specific node for this tree, check the link ajsm.");
//       								return;
//       							}
//       							for(var i=0;i<modules.length;i++){
//       								higlightAgentAjsms(modules[i].idNode,$scope.data[0].nodes);
//       							}
//       						});
//       					}
//       				});
//    			}else{
          ModulesService.getModuleFragmentByModuleId(agentData[0].idNode).then(function(response) {
            if(response.status == '200') {
              // loop each fragment get details for each, filter agents
              _.each(response.data, function(data) {
                promises.push(populateChildNodesOfFragmentFilterAgents(data, fragments, idAgent));
              });
              $q.all(promises).then(function() {
                if(agentData[0].nodes.length < 1 && fragments.length < 1) {
                  ngToast.create({
                    className: 'danger',
                    content: "There is no study specific node for this tree, check the link task module.",
                    animation: 'slide'
                  });
                  return;
                }
                for(var i = 0; i < fragments.length; i++) {
                  higlightAgentAjsms(fragments[i].idNode, $scope.data[0].nodes);
                }
              });
            }
          });
//    			}
        }
      });
    }

    function higlightAgentAjsms(fragmentIdNode, childNodes) {
      _.each(childNodes, function(item) {
        if(item.link == fragmentIdNode) {
          angular.element("#node-" + item.idNode).addClass("nodeAgentEnabledLinked");
        }
        if(item.nodes.length > 0) {
          higlightAgentAjsms(fragmentIdNode, item.nodes);
        }
      });
    }

    function highlightParentNodeWithAgent(idNode) {
      var nodeResult = findNodeById(idNode, $scope.data[0]);
      if(nodeResult) {
        if(nodeResult.idNode == $scope.data[0].idNode) {
          return;
        }
        if(nodeResult.type.match('Q_')) {
          //highlight all child
          _.each(nodeResult.nodes, function(n) {
            highlightChildNodeWithAgent(n.idNode);
          });
        }
        angular.element("#node-" + idNode).addClass("nodeAgentEnabledDerived");
        highlightParentNodeWithAgent(nodeResult.parentId);
      }
    }

    function highlightChildNodeWithAgent(idNode) {
      angular.element("#node-" + idNode).addClass("nodeAgentEnabledBase");
    }

    function highlightAgentSpecificNode(childNodes) {
      for(var i = 0; i < childNodes.length; i++) {
        var node = childNodes[i];
        if(!node.link) {
          angular.element("#node-" + node.idNode).addClass("nodeAgentEnabledBase");
          if(node.nodes) {
            highlightAgentSpecificNode(node.nodes);
          }
        }
      }
    }

    function higlightNodesWithAgentAjsms(fragmentIdNode, childNodes) {
      _.each(childNodes, function(item) {
        if(item.link == fragmentIdNode) {
          angular.element("#node-" + item.idNode).addClass("nodeAgentEnabledLinked");
        }
        if(item.nodes.length > 0) {
          higlightNodesWithAgentAjsms(fragmentIdNode, item.nodes);
        }
      });
    }

    function findNodeById(idNode, node) {
      var result = undefined;
      if(node.idNode == idNode) {
        result = node;
      }

      if(result) {
        return result;
      } else {
        if(node.nodes) {
          for(var i = 0; i < node.nodes.length; i++) {
            result = findNodeById(idNode, node.nodes[i]);
            if(result) {
              return result;
            }
          }
        }
      }
    }

    function fn(obj, key) {
      if(_.has(obj, key)) // or just (key in obj)
        return [obj];
      // elegant:
      return _.flatten(_.map(obj, function(v) {
        return v instanceof Array ? fn(v, key) : [];
      }), true);

      // or efficient:
//    	    var res = [];
//    	    _.forEach(obj, function(v) {
//    	        if (typeof instanceof Array && (v = fn(v, key)).length)
//    	            res.push.apply(res, v);
//    	    });
//    	    return res;
    }

    var rootChildNodes = undefined;
    var rootIndex = 0;
    var count = 0;

    function printPdf(node, pdf, count, name) {
      var loc = $("#node-" + node.idNode);
      count = 0;
      rootChildNodes = node.nodes;
      rootIndex = 0;
      processedPdfNode = [];
      var endNode = getLastNode(node);
      printChildNode(node, pdf, count, endNode.idNode, name);
    }

    var deepChildNode = undefined;
    var deepChildNodeIndex = 0;
    var processedPdfNode = [];

    function printChildNode(node, pdf, count, endNode, name, lastChildNode,
                            rootChildNodes, rootIndex, deepChildNode, deepChildNodeIndex) {
      var loc = $("#node-" + node.idNode).get(0);
      if(node.nodes.length > 0) {
        var ln = getLastNode(node);
      }
      var lastChildNode = lastChildNode;
      if(ln) {
        lastChildNode = ln.idNode;
      }
      if(processedPdfNode.indexOf(node.idNode) > -1 && node.parentId) {
        var parentNode = findNodeById(node.parentId, data[0]);
        var unprocessedFound = false;
        for(var i = 0; i < parentNode.nodes.length; i++) {
          if(processedPdfNode.indexOf(parentNode.nodes[i].idNode) == -1) {
            unprocessedFound = true;
            return printChildNode(parentNode.nodes[i], pdf,
              count, endNode, name, lastChildNode,
              rootChildNodes, rootIndex,
              deepChildNode, deepChildNodeIndex);
            break;
          }
        }
        if(!unprocessedFound) {
          return printChildNode(parentNode, pdf,
            count, endNode, name, lastChildNode,
            rootChildNodes, rootIndex,
            deepChildNode, deepChildNodeIndex)
        }
      }
      html2pdf(loc, pdf, function(pdf) {
        var elementId = 'node-' + node.idNode;
        processedPdfNode.push(node.idNode);
        console.log(processedPdfNode[processedPdfNode.length - 1]);
        count = count + 1;
//    		  if(endNode == node.idNode){
//    			  pdf.save(name + '.pdf');
//    			  return;
//    		  }
//    		  if(count % 30 == 0){
//    			  pdf.addPage();
//    		  }
        if(count % 35 == 0) {
          _.each(processedPdfNode, function(idNode) {
            $("#node-" + idNode).hide();
          });
          pdf.addPage();
        }
        if(endNode == node.idNode) {
          pdf.save(name + '.pdf');
          return;
        }
        if(node.idNode == data[0].idNode) {
          if(node.nodes.length > 0) {
            rootChildNodes = node.nodes;
            return printChildNode(node.nodes[0], pdf,
              count, endNode, name, lastChildNode,
              rootChildNodes, 0,
              deepChildNode, deepChildNodeIndex);
          }
        } else if(node.idNode == lastChildNode) {
          //get parent
          var parentNode = findNodeById(node.parentId, data[0]);
          var unprocessedFound = false;
          for(var i = 0; i < parentNode.nodes.length; i++) {
            if(processedPdfNode.indexOf(parentNode.nodes[i].idNode) == -1) {
              unprocessedFound = true;
              return printChildNode(parentNode.nodes[i], pdf,
                count, endNode, name, lastChildNode,
                rootChildNodes, rootIndex,
                deepChildNode, deepChildNodeIndex);
              break;
            }
          }
          if(!unprocessedFound) {
            return printChildNode(parentNode, pdf,
              count, endNode, name, lastChildNode,
              rootChildNodes, rootIndex,
              deepChildNode, deepChildNodeIndex)
          }

        } else if(node.nodes.length > 0) {
          //check if node has child nodes
          var deepChildNode = node.nodes;
          var deepChildNodeIndex = 0;
          return printChildNode(node.nodes[0], pdf,
            count, endNode, name, lastChildNode,
            rootChildNodes, rootIndex,
            deepChildNode, deepChildNodeIndex);
        } else {
          var parentNode = findNodeById(node.parentId, data[0]);
          var unprocessedFound = false;
          for(var i = 0; i < parentNode.nodes.length; i++) {
            if(processedPdfNode.indexOf(parentNode.nodes[i].idNode) == -1) {
              unprocessedFound = true;
              return printChildNode(parentNode.nodes[i], pdf,
                count, endNode, name, lastChildNode,
                rootChildNodes, rootIndex,
                deepChildNode, deepChildNodeIndex);
              break;
            }
          }
          if(!unprocessedFound) {
            return printChildNode(parentNode, pdf,
              count, endNode, name, lastChildNode,
              rootChildNodes, rootIndex,
              deepChildNode, deepChildNodeIndex)
          }
        }
      });
    }

    function getLastNode(node) {
      if(node.nodes) {
        //get last child node
        var lastNode = node.nodes[node.nodes.length - 1];
        if(lastNode.nodes.length > 0) {
          return getLastNode(lastNode);
        } else {
          return lastNode;
        }
      } else {
        return node;
      }
    }

    $(window).scroll(function() {
      if($(this).scrollTop() > 50) {
        $('#back-to-top').fadeIn();
      } else {
        $('#back-to-top').fadeOut();
      }
    });
    // scroll body to 0px on click
    $scope.scrollToTop = function() {
      $('body,html').animate({
        scrollTop: 0
      }, 800);
    };

    var loadNodes = function() {
      bsLoadingOverlayService.start();
      for(var i = 1; i < totalIndexCount; i++) {
        (function(i) {
          $timeout(function() {
            loadChildNodes(i);
          }, 0);
        })(i)
      }
      if(totalIndexCount < 1) {
        bsLoadingOverlayService.stop();
      }
    };

    var loadChildNodes = function(i) {
      $scope.safeApply(function() {
        self.node[0].nodes.push(self.data[0].nodes[i]);
        indexCount++;
        var childLength = getNumberOfChildNode(self.data[0].nodes[i], 0);
        if(i >= self.data[0].nodes.length - 1) {
          bsLoadingOverlayService.stop();
        }

      });
    };

    function getNumberOfChildNode(n, count) {
      count = count + n.nodes.length;
      _.each(n.nodes, function(x) {
        count = getNumberOfChildNode(x, count);
      });
      return count;
    }

//       $timeout(function() {
//    	   loadNodes();
//       }, 1000);


  }
})();