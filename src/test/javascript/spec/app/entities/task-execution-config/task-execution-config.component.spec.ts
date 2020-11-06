import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PipelineTestModule } from '../../../test.module';
import { TaskExecutionConfigComponent } from 'app/entities/task-execution-config/task-execution-config.component';
import { TaskExecutionConfigService } from 'app/entities/task-execution-config/task-execution-config.service';
import { TaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

describe('Component Tests', () => {
  describe('TaskExecutionConfig Management Component', () => {
    let comp: TaskExecutionConfigComponent;
    let fixture: ComponentFixture<TaskExecutionConfigComponent>;
    let service: TaskExecutionConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [TaskExecutionConfigComponent],
      })
        .overrideTemplate(TaskExecutionConfigComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskExecutionConfigComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskExecutionConfigService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TaskExecutionConfig(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.taskExecutionConfigs && comp.taskExecutionConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
